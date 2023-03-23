class Operation {
    constructor() {
    }

    evaluate(x, y, z) {
        return this.evaluatingRule(...this.operands.map(el => el.evaluate(x, y, z)));
    }

    toString() {
        return this.operands.map(x => x.toString()).reduce((a, b) => a + " " + b) + " " + this.symbol;
    }

    prefix() {
        return "(" + this.symbol + " " + this.operands.map(x => x.prefix()).reduce((a, b) => a + " " + b) + ")";
    }
}

class Add extends Operation {
    constructor(...terms) {
        super();
        this.operands = terms;
        this.symbol = '+';
    }

    evaluatingRule(...terms) {
        return terms.reduce((a, b) => a + b);
    }

    diff(by) {
        return new Add(...this.operands.map(a => a.diff(by)))
    }
}

class Subtract extends Operation {
    constructor(minuend, subtracted) {
        super();
        this.operands = [minuend, subtracted];
        this.symbol = '-';
    }

    evaluatingRule(minuend, subtracted) {
        return minuend - subtracted;
    }

    diff(by) {
        return new Subtract(this.operands[0].diff(by), this.operands[1].diff(by));
    }
}

class Multiply extends Operation {
    constructor(mul1, mul2) {
        super();
        this.operands = [mul1, mul2];
        this.symbol = '*';
    }

    evaluatingRule(mul1, mul2) {
        return mul1 * mul2;
    }

    diff(by) {
        return new Add(
            new Multiply(this.operands[0].diff(by), this.operands[1]),
            new Multiply(this.operands[0], this.operands[1].diff(by))
        );
    }
}

class Divide extends Operation {
    constructor(dividend, divider) {
        super();
        this.operands = [dividend, divider];
        this.symbol = '/';
    }

    evaluatingRule(dividend, divider) {
        return dividend / divider;
    }

    diff(by) {
        return new Divide(
            new Subtract(
                new Multiply(this.operands[0].diff(by), this.operands[1]),
                new Multiply(this.operands[0], this.operands[1].diff(by))
            ),
            new Multiply(this.operands[1], this.operands[1])
        );
    }
}

class Negate extends Operation {
    constructor(operand) {
        super();
        this.operands = [operand];
        this.symbol = 'negate';
    }

    evaluatingRule(operand) {
        return -operand;
    }

    diff(by) {
        return new Negate(this.operands[0].diff(by));
    }
}

class Const {
    constructor(val) {
        this.val = val;
    }

    evaluate() {
        return this.val;
    }

    toString() {
        return this.val.toString();
    }

    diff() {
        return new Const(0);
    }

    prefix = this.toString;
}

const AVAILABLE_VARIABLES = [
    'x', 'y', 'z'
];

class Variable {
    constructor(name) {
        this.name = name;
    }

    evaluate(...values) {
        return values[AVAILABLE_VARIABLES.indexOf(this.name)]
    }

    toString() {
        return this.name;
    }

    diff(by) {
        return new Const(by === this.name ? 1 : 0);
    }

    prefix = this.toString;
}

class SumrecN extends Operation {
    constructor(...operands) {
        super();
        this.operands = operands;
        this.symbol = 'sumrec' + operands.length;
        this.exprItself = new Add(...operands.map(a => new Divide(new Const(1), a)));
    }

    evaluate(x, y, z) {
        return this.exprItself.evaluate(x, y, z);
    }

    diff(by) {
        return this.exprItself.diff(by);
    }
}

class Sumrec2 extends SumrecN {
    constructor(...operands) {
        super(...operands);
    }
}

class Sumrec3 extends SumrecN {
    constructor(...operands) {
        super(...operands);
    }
}

class Sumrec4 extends SumrecN {
    constructor(...operands) {
        super(...operands);
    }
}

class Sumrec5 extends SumrecN {
    constructor(...operands) {
        super(...operands);
    }
}

class HMeanN extends Operation {
    constructor(...operands) {
        super();
        this.operands = operands;
        this.symbol = 'hmean' + operands.length;
        this.exprItself = new Divide(new Const(operands.length), new SumrecN(...operands));
    }

    evaluate(x, y, z) {
        return this.exprItself.evaluate(x, y, z);
    }

    diff(by) {
        return this.exprItself.diff(by);
    }
}

class HMean2 extends HMeanN {
    constructor(...operands) {
        super(...operands);
    }
}

class HMean3 extends HMeanN {
    constructor(...operands) {
        super(...operands);
    }
}

class HMean4 extends HMeanN {
    constructor(...operands) {
        super(...operands);
    }
}

class HMean5 extends HMeanN {
    constructor(...operands) {
        super(...operands);
    }
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

class ParseError extends Error {
    constructor(message, pos) {
        super("At pos " + pos + ": " + message);
    }
}

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

class PrefixParser extends Reader {

    constructor(expr) {
        super(expr);
    }

    parse() {
        let parsedExpr = this.parseParentheses();
        this.trim();
        if (!this.checkEOF()) {
            throw new ParseError("Expected EOF, but found: '" + this.nextChar() + "'", this.getPos());
        }
        return parsedExpr;
    }

    parseParentheses() {
        this.trim();
        if (this.test('(')) {
            this.nextChar();
            let parsedExpr = this.parseParentheses();
            this.trim();
            if (!this.test(')')) {
                throw new ParseError("expected ')', but found '" + this.nextChar() + "'", this.getPos());
            }
            this.nextChar();
            return parsedExpr;
        }
        return this.parseExpr();
    }

    parseExpr() {
        this.trim();
        let currentToken = this.parseToken();

        if (AVAILABLE_VARIABLES.includes(currentToken)) {
            return new Variable(currentToken);
        } else if (currentToken && !isNaN(currentToken)) {
            return new Const(parseInt(currentToken));
        } else if (currentToken in OPERATIONS) {
            let operands = this.parseOperands();
            if (!((OPERATIONS[currentToken][1] === -1 && operands.length > 0) ||
                operands.length === OPERATIONS[currentToken][1])) {
                throw new ParseError("unexpected number of operands for '" + currentToken
                    + "', found " + operands.length, this.getPos());
            }
            return new OPERATIONS[currentToken][0](...operands);
        } else {
            throw new ParseError("unexpected token '" + currentToken + "'", this.getPos());
        }
    }

    parseToken() {
        let currentToken = "";
        while (!this.test(" ") && !this.test(')') && !this.test('(') && !this.checkEOF()) {
            currentToken += this.nextChar();
        }
        return currentToken;
    }

    parseOperands() {
        let operands = [];
        while (!this.checkEOF() && !this.test(')')) {
            operands.push(this.parseParentheses());
            this.trim();
        }
        return operands;
    }
}

let parsePrefix = expr => new PrefixParser(expr).parse();
