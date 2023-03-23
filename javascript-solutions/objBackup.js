function Operation() {
    this.operands = operands;
    this.evaluatingRule = evaluatingRule;
    this.symbol = symbol;
    this.diff = diffRule;
}

Operation.prototype.evaluate = function (x, y, z) {
    return this.evaluatingRule(...this.operands.map(el => el.evaluate(x, y, z)));
}

Operation.prototype.toString = function () {
    return this.operands.map(x => x.toString()).reduce((a, b) => a + " " + b) + " " + this.symbol;
}

Operation.prototype.prefix = function () {
    return "(" + this.symbol + " " + this.operands.map(x => x.prefix()).reduce((a, b) => a + " " + b) + ")";
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
    this.prefix = this.toString;
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
    this.prefix = this.toString;
}


for (let obj of [Add, Subtract, Multiply, Divide, Negate, SumrecN, Sumrec2, Sumrec3, Sumrec4, Sumrec5,
    HMeanN, HMean2, HMean3, HMean4, HMean5]) {
    obj.prototype = Object.create(Operation.prototype);
}

const OPERATIONS = {
    '+': [Add, 2, 2],
    '-': [Subtract, 2, 2],
    '*': [Multiply, 2, 2],
    '/': [Divide, 2, 2],
    'negate': [Negate, 1, 1],
    'sumrec2': [Sumrec2, 2, 2],
    'sumrec3': [Sumrec3, 3, 3],
    'sumrec4': [Sumrec4, 4, 4],
    'sumrec5': [Sumrec5, 5, 5],
    'hmean2': [HMean2, 2, 2],
    'hmean3': [HMean3, 3, 3],
    'hmean4': [HMean4, 4, 4],
    'hmean5': [HMean5, 5, 5],
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

function ParseError(message, pos) {
    this.message = "At pos " + pos + ": " + message;
}

ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = "ParseError";
ParseError.prototype.constructor = ParseError;

class Reader {
    constructor(string) {
        this.string = string;
        this.position = 0;
    }

    test(char) {
        if (!this.checkEOF()) {
            return this.string[this.position] === char;
        }
    }

    nextChar() {
        if (!this.checkEOF()) {
            return this.string[this.position++];
        }
    }

    checkEOF() {
        return this.position === this.string.length;
    }

    trim() {
        while (this.test(" ")) {
            this.position++;
        }
    }

    getPos() {
        return this.position;
    }
}

let parsePrefix = expr => {
    let reader = new Reader(expr);
    let parsedExpr = parsePrefixImpl(reader);
    reader.trim();
    if (!reader.checkEOF()) {
        throw new ParseError("Expected EOF, but found: '" + reader.nextChar() + "'", reader.getPos());
    }
    return parsedExpr;
}

let parsePrefixImpl = reader => {
    reader.trim();
    let inParentheses = false;
    if (reader.test('(')) {
        reader.nextChar();
        inParentheses = true;
        reader.trim();
    }

    let currentToken = "";
    while (!reader.test(" ") && !reader.test(')') && !reader.test('(') && !reader.checkEOF()) {
        currentToken += reader.nextChar();
    }

    let result;
    if (AVAILABLE_VARIABLES.includes(currentToken)) {
        result = new Variable(currentToken);
    } else if (currentToken && !isNaN(currentToken)) {
        result = new Const(parseInt(currentToken));
    } else if (currentToken in OPERATIONS) {
        let opers = [];
        while (!reader.checkEOF() && !reader.test(')')) {
            opers.push(parsePrefixImpl(reader));
            reader.trim();
        }
        if (! (opers.length >= OPERATIONS[currentToken][1] && opers.length <= OPERATIONS[currentToken][2])) {
            throw new ParseError("unexpected number of operands for '" + currentToken
                + "', found " + opers.length, reader.getPos());
        }
        result = new OPERATIONS[currentToken][0](...opers);
    } else {
        throw new ParseError("unexpected token '" + currentToken + "'", reader.getPos());
    }

    if (inParentheses) {
        reader.trim();
        if (!reader.test(')')) {
            throw new ParseError("expected ')', but found '" + reader.nextChar() + "'", reader.getPos());
        }
        reader.nextChar();
    }
    return result;
}
