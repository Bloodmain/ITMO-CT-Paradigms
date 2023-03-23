function Operation(symbol, evaluatingRule, diffRule, ...operands) {
    this.evaluate = (x, y, z) => evaluatingRule(...operands.map(el => el.evaluate(x, y, z)));
    this.toString = () => operands.map(x => x.toString()).reduce((a, b) => a + " " + b) + " " + symbol;
    this.diff = diffRule;
}

function Add(...terms) {
    Operation.call(
        this, '+',
        (...operands) => operands.reduce((a, b) => a + b),
        (by) => new Add(...terms.map(a => a.diff(by))),
        ...terms
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

function SumrecN(symbol, ...operands) {
    let exprItself = new Add(...operands.map(a => new Divide(new Const(1), a)));
    Operation.call(
        this, symbol,
        (...args) => args.reduce((a, b) => a + 1 / b, 0),
        (by) => exprItself.diff(by),
        ...operands
    );
}

function Sumrec2(...operands) {
    SumrecN.call(this, 'sumrec2', ...operands);
}

function Sumrec3(...operands) {
    SumrecN.call(this, 'sumrec3', ...operands);
}

function Sumrec4(...operands) {
    SumrecN.call(this, 'sumrec4', ...operands);
}

function Sumrec5(...operands) {
    SumrecN.call(this, 'sumrec5', ...operands);
}

function HMeanN(symbol, ...operands) {
    let exprItself = new Divide(
        new Const(operands.length),
        new SumrecN('sumrec' + operands.length, ...operands)
    );
    Operation.call(
        this, symbol,
        (...args) => operands.length / args.reduce((a, b) => a + 1 / b, 0),
        (by) => exprItself.diff(by),
        ...operands
    );
}

function HMean2(...operands) {
    HMeanN.call(this, 'hmean2', ...operands);
}

function HMean3(...operands) {
    HMeanN.call(this, 'hmean3', ...operands);
}

function HMean4(...operands) {
    HMeanN.call(this, 'hmean4', ...operands);
}

function HMean5(...operands) {
    HMeanN.call(this, 'hmean5', ...operands);
}

const AVAILABLE_VARIABLES = [
    'x', 'y', 'z'
];

function Variable(name) {
    this.evaluate = (...values) => values[AVAILABLE_VARIABLES.indexOf(name)];
    this.toString = () => name;
    this.diff = (by) => new Const(by === name ? 1 : 0);
}

const OPERATIONS = {
    '+': [Add, 2],
    '-': [Subtract, 2],
    '*': [Multiply, 2],
    '/': [Divide, 2],
    'negate': [Negate, 1],
    'sumrec2': [Sumrec2, 2],
    'sumrec3': [Sumrec3, 3],
    'sumrec4': [Sumrec4, 4],
    'sumrec5': [Sumrec5, 5],
    'hmean2': [HMean2, 2],
    'hmean3': [HMean3, 3],
    'hmean4': [HMean4, 4],
    'hmean5': [HMean5, 5],
};

let parse = expr => {
    return parseImpl(expr.trim().split(/\s+/));
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

// console.log(new HMean2(new Const(1), new Const(2)).diff('x').evaluate(2,2,2));