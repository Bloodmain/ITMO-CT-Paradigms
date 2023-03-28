class Operation {
    operands;
    symbol;

    constructor() {
    }

    evaluate(x, y, z) {
        return this.evaluatingRule(...this.operands.map(el => el.evaluate(x, y, z)));
    }

    operandsToString(stringFunction) {
        return this.operands.map(x => x[stringFunction]()).join(" ");
    }

    toString() {
        return this.operandsToString("toString") + " " + this.symbol;
    }

    prefix() {
        return "(" + this.symbol + " " + this.operandsToString("prefix") + ")";
    }

    postfix() {
        return "(" + this.operandsToString("postfix") + " " + this.symbol + ")";
    }
}

class ComposedOperation extends Operation {
    composedFrom;

    evaluate(x, y, z) {
        return this.composedFrom.evaluate(x, y, z);
    }

    diff(by) {
        return this.composedFrom.diff(by);
    }
}

class Add extends Operation {
    constructor(...terms) {
        super();
        this.operands = terms;
        this.symbol = '+';
    }

    evaluatingRule(...terms) {
        return terms.reduce((a, b) => a + b, 0);
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
    postfix = this.toString;
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
    postfix = this.toString;
}

class SumrecN extends ComposedOperation {
    constructor(...operands) {
        super();
        this.operands = operands;
        this.symbol = 'sumrec' + operands.length;
        this.composedFrom = new Add(...operands.map(a => new Divide(new Const(1), a)));
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

class HMeanN extends ComposedOperation {
    constructor(...operands) {
        super();
        this.operands = operands;
        this.symbol = 'hmean' + operands.length;
        this.composedFrom = new Divide(new Const(operands.length), new SumrecN(...operands));
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

class Square extends ComposedOperation {
    constructor(operand) {
        super();
        this.operands = [operand];
        this.symbol = 'square';
        this.composedFrom = new Multiply(operand, operand);
    }
}

class SquareRoot extends Operation {
    constructor(operand) {
        super();
        this.operands = [operand];
        this.symbol = 'sqrt';
    }

    evaluatingRule(operand) {
        return Math.sqrt(operand);
    }

    diff(by) {
        return new Divide(
            this.operands[0].diff(by),
            new Multiply(new Const(2), this)
        );
    }
}

class Meansq extends ComposedOperation {
    constructor(...operands) {
        super();
        this.operands = operands;
        this.symbol = 'meansq';
        this.composedFrom = new Divide(
            new Add(...operands.map(el => new Square(el))),
            new Const(this.operands.length)
        );
    }
}

class RMS extends ComposedOperation {
    constructor(...operands) {
        super();
        this.operands = operands;
        this.symbol = 'rms';
        this.composedFrom = new SquareRoot(new Meansq(...operands));
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
    'meansq': [Meansq, -1],
    'rms': [RMS, -1]
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
    static END = '\0';

    constructor(string) {
        this.string = string;
        this.position = 0;
    }

    test(char) {
        return !this.checkEOF() && this.string[this.position] === char;
    }

    nextChar() {
        return this.checkEOF() ? Reader.END : this.string[this.position++];
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

class ExprParser extends Reader {

    constructor(expr, dialectOperatorIndex) {
        super(expr);
        this.dialectOperatorIndex = dialectOperatorIndex;
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
            this.trim();
            let parsedExpr = this.parseOperation();
            this.trim();
            if (!this.test(')')) {
                throw new ParseError("expected ')', but found '" + this.nextChar() + "'", this.getPos());
            }
            this.nextChar();
            return parsedExpr;
        }
        return this.parseExpr();
    }

    parseOperation() {
        let tokens = this.parseOperands();
        let operation = tokens.at(this.dialectOperatorIndex);
        if (operation === undefined) {
            throw new ParseError("expected operation at pos " + this.dialectOperatorIndex +
                ", but there are too few operands", this.getPos());
        }
        if (operation in OPERATIONS) {
            tokens.splice(this.dialectOperatorIndex, 1);
            if (!this.checkOperandsNumber(operation, tokens.length)) {
                throw new ParseError("unexpected number of operands for '" + operation
                    + "', found " + tokens.length, this.getPos());
            }
            return new OPERATIONS[operation][0](...tokens);
        } else {
            throw new ParseError("expected operation, found: '" + operation + "'", this.getPos())
        }
    }

    parseExpr() {
        let currentToken = this.parseToken();
        if (AVAILABLE_VARIABLES.includes(currentToken)) {
            return new Variable(currentToken);
        } else if (currentToken && !isNaN(currentToken)) {
            return new Const(parseInt(currentToken));
        } else if (currentToken in OPERATIONS) {
            return currentToken;
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
        let operationFound = false;
        while (!this.checkEOF() && !this.test(')')) {
            let operand = this.parseParentheses();
            if (operand in OPERATIONS) {
                if (operationFound) {
                    throw new ParseError("unexpected second operation '" + operand + "'", this.getPos());
                } else {
                    operationFound = true;
                }
            }
            operands.push(operand);
            this.trim();
        }
        return operands;
    }

    checkOperandsNumber(operation, operandsCount) {
        return operandsCount === OPERATIONS[operation][1] || (OPERATIONS[operation][1] === -1);
    }
}

let parsePrefix = expr => new ExprParser(expr, 0).parse();
let parsePostfix = expr => new ExprParser(expr, -1).parse();
