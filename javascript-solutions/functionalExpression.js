"use strict";

let operator = operator => (...args) => (x, y, z) => operator(...args.map(el => el(x, y, z)));

let add = operator((a, b) => a + b);
let subtract = operator((a, b) => a - b);
let multiply = operator((a, b) => a * b);
let divide = operator((a, b) => a / b);
let negate = operator(a => -a);
let cnst = val => () => val;
let variable = name => (...values) => values[availableVariables.indexOf(name)];

const operations = {
    '+': [add, 2],
    '-': [subtract, 2],
    '*': [multiply, 2],
    '/': [divide, 2],
    'negate': [negate, 1]
};

const availableVariables = [
    'x', 'y', 'z'
];

let parse = expr => {
    let parsedExpr = parseImpl(expr.trim().split(' '));
    return (x, y, z) => parsedExpr(x, y, z);
}
let parseImpl = elements => {
    let lastElement;
    do {
        lastElement = elements.pop();
    } while (lastElement.length === 0);
    if (availableVariables.includes(lastElement)) {
        return variable(lastElement);
    }
    if (lastElement in operations) {
        let operands = [];
        for (let i = 0; i < operations[lastElement][1]; ++i) {
            operands.unshift(parseImpl(elements));
        }
        return operations[lastElement][0](...operands);
    }
    return cnst(parseInt(lastElement));
}

// let expression = parse('1 x x * x 2 * - +');
// for (let x = 0; x <= 10; ++x) {
//     println("x = " + x + "; expression(x) = " + expression(x, 0, 0));
// }
