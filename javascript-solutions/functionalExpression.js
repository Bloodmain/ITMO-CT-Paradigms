"use strict";

let operator = operator => (...args) => (x, y, z) => operator(...args.map(el => el(x, y, z)));

let add = operator((a, b) => a + b);
let subtract = operator((a, b) => a - b);
let multiply = operator((a, b) => a * b);
let divide = operator((a, b) => a / b);
let negate = operator(a => -a);
let cnst = val => () => val;
let variable = name => (...values) => values[AVAILABLE_VARIABLES.indexOf(name)];
let one = cnst(1);
let two = cnst(2);

let findCompInd = comparator => (...args) => args.indexOf(comparator(...args));
let argComp = comparator => operator(findCompInd(comparator));
let argMin = argComp(Math.min);
let argMax = argComp(Math.max);
let argMin3 = argMin;
let argMax3 = argMax;
let argMin5 = argMin;
let argMax5 = argMax;

const OPERATIONS = {
    '+': [add, 2],
    '-': [subtract, 2],
    '*': [multiply, 2],
    '/': [divide, 2],
    'negate': [negate, 1],
    'argMin3': [argMin3, 3],
    'argMax3': [argMax3, 3],
    'argMin5': [argMin5, 5],
    'argMax5': [argMax5, 5]
};

const AVAILABLE_VARIABLES = [
    'x', 'y', 'z'
];

const AVAILABLE_CONSTS = {
    'one': one,
    'two': two
}

let parse = expr => {
    let parsedExpr = parseImpl(expr.trim().split(' ').filter(el => el.length > 0));
    return (x, y, z) => parsedExpr(x, y, z);
}

let parseImpl = elements => {
    let lastElement = elements.pop();
    if (AVAILABLE_VARIABLES.includes(lastElement)) {
        return variable(lastElement);
    } else if (lastElement in AVAILABLE_CONSTS) {
        return AVAILABLE_CONSTS[lastElement];
    } else if (lastElement in OPERATIONS) {
        let operands = Array(OPERATIONS[lastElement][1]).fill(0).reduce(
            (acc, _) => [parseImpl(elements)].concat(acc),
            []
        );
        return OPERATIONS[lastElement][0](...operands);
    }
    return cnst(parseInt(lastElement));
}
