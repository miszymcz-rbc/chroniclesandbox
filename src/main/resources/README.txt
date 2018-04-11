
Glossary:
High quality software - nearly bug-free and maintainable software





























---

1. Testing after: Writing production code, then writing tests.
2. TDD: testing first + production code driven by requirements and tests.





























---

1. Testing after - Test that your solution does what you think it does.
2. TDD - Test that you solution solves the problem you were asked to solve.

TDD Decouples problem space from the solution space.
Defining a problem is as important as implementing a solution.
You are free to redesign/simplify you solution as long as it solves the problem.





























---

3 rules of TDD:
1. You are not allowed to write any production code unless it is to make a failing unit test pass.
2. You are not allowed to write any more of a unit test than is sufficient to fail; and compilation failures are failures.
3. You are not allowed to write any more production code than is sufficient to pass the one failing unit test.

TDD cycle:
Red -> Green -> Refactor





























---

You can use TDD feedback loop to:

- understand/challenge the problem you want to solve
- learn how to write high-quality tests
- write testable production code
- write as little production code as possible

The feedback loop is most often in the form of a pain you feel during development.





























---

Without TDD:
- Writing high quality production code is hard

With TDD:
- Writing high quality test code is hard
- Writing high quality production code is hard

Seems like twice as much work to me...





























---

It's all about the feedback loops.

Without reliable high-quality tests.

|
|
|   X       X       X
|   X       X       X
------------------------> time (=cost)
 seconds | hours | days





























---

It's all about the feedback loops.

IF you listen to the TDD pain feedback loop you will have better tests.
IF you have better tests, you may have same number of bugs, but your tests catch them within seconds.
IF you catch majority of the bugs within seconds, right after you introduced them
IF you catch them within seconds, you do Ctrl+Z and spend not time debugging.
If you reverted most of you bugs before they were checked in, you team is not slowed down by them.
IF you reverted most of your bugs before they were checked in, you spend less time troubleshooting.

Bugs discovered since introduced.
With reliable high-quality tests.

|  O
|  O
|  O
|  O        O       O
------------------------> time (=cost)
 seconds | hours | days





























---

Let's apply it to the low-latency apps?

Our goal:

Latency issues discovered since introduced.
With reliable high-quality tests.

|  O
|  O
|  O
|  O        O       O
------------------------> time (=cost)
 seconds | hours | days
