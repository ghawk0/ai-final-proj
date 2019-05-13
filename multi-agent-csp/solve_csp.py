import era
import random
import sys

"""
Script that solves an instance of the n-queens CSP problem. 
"""

def queens_problem(n):
    """
    Obtain domains for the n-queen problem.
    :param n: Number of queens n.
    :return:  Domains of each queen. Each variable represents a single row and the domain
              is all possible columns in that row.
    """
    domains = {}

    for i in range(n):
        domains[i] = [i + 1 for i in range(n)]

    return domains


def queen_violation(p1, p2, E = None):
    """
    Violation function for the n-queens problem.
    :param p1: Pos namedtuple representing the position (x1, y1) in the environment.
    :param p2: Pos namedtuple representing the position (x2, y2) in the environment.
    :param E:  Not used (but given
    :return:   x1 != x2 ^ |x1 - x2| != |y1 - y2|
    """
    if p1.y == p2.y:
        return False

    return p1.x == p2.x or abs(p1.x - p2.x) == abs(p1.y - p2.y)


def main():

    if len(sys.argv) < 2:
        print("Please specify a number of queens. ")
        sys.exit()

    n = int(sys.argv[1])

    D = queens_problem(n)
    E, A = era.get_environment(D)

    era.era(E, A, queen_violation)

    if era.is_solution(E, A):
        print("Solution: ")
        for a in A:
            print("Agent {}: {}".format(a.row + 1, E[a.row][a.x].val))


if __name__ == '__main__':
    main()