"use strict";
include("functionalExpression.js");

let expression = parse('1 x x * x 2 * - +');
for (let x = 0; x <= 10; ++x) {
    println("x = " + x + "; expression(x) = " + expression(x, 0, 0));
}
