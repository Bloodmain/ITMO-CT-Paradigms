function Operation(symbol, evaluatingRule, diffRule, ...operands) {
    this.evaluate = (x, y, z) => evaluatingRule(...operands.map(el => el.evaluate(x, y, z)));
    this.toString = () => operands.map(x => x.toString()).reduce((a, b) => a + " " + b) + " " + symbol;
    this.diff = diffRule;
}

function Add(term1, term2) {
    Operation.call(
        this, '+',
        (a, b) => a + b,
        (by) => new Add(term1.diff(by), term2.diff(by)),
        term1, term2
    );
}

function Subtract(minuend, subtracted) {
    Operation.call(
        this, '-',
        (a, b) => a - b,
        (by) => new Subtract(minuend.diff(by), subtracted.diff(by)),
        minuend, subtracted
    );
}

function Multiply(mul1, mul2) {
    Operation.call(
        this, '*',
        (a, b) => a * b,
        (by) => new Add(
            new Multiply(mul1.diff(by), mul2),
            new Multiply(mul1, mul2.diff(by))
        ),
        mul1, mul2
    );
}

function Divide(dividend, divider) {
    Operation.call(
        this, '/',
        (a, b) => a / b,
        (by) => new Divide(
            new Subtract(new Multiply(dividend.diff(by), divider), new Multiply(dividend, divider.diff(by))),
            new Multiply(divider, divider)
        ),
        dividend, divider
    );
}

function Negate(operand) {
    Operation.call(
        this, 'negate',
        (a) => -a,
        (by) => new Negate(operand.diff(by)),
        operand
    );
}

function Const(val) {
    this.evaluate = () => val;
    this.toString = () => val.toString();
    this.diff = () => new Const(0);
}

const AVAILABLE_VARIABLES = [
    'x', 'y', 'z'
];

function Variable(name) {
    this.evaluate = (...values) => values[AVAILABLE_VARIABLES.indexOf(name)];
    this.toString = () => name;
    this.diff = (by) => new Const(by === name ? 1 : 0);
};

const OPERATIONS = {
    '+': [Add, 2],
    '-': [Subtract, 2],
    '*': [Multiply, 2],
    '/': [Divide, 2],
    'negate': [Negate, 1]
};

let parse = expr => {
    return parseImpl(expr.trim().split(' ').filter(el => el.length > 0));
};

let parseImpl = elements => {
    let lastElement = elements.pop();
    if (AVAILABLE_VARIABLES.includes(lastElement)) {
        return new Variable(lastElement);
    } else if (lastElement in OPERATIONS) {
        let operands = Array(OPERATIONS[lastElement][1]).fill(0).reduce(
            (acc, _) => [parseImpl(elements)].concat(acc),
            []
        );
        return new OPERATIONS[lastElement][0](...operands);
    }
    return new Const(parseInt(lastElement));
};