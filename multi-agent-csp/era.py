import random
from collections import namedtuple

RANDOM_P = 0.1
LEAST_P = 0.54
BETTER_P = .36

Pos = namedtuple('Pos', 'x y')

class Agent:
    """
    Class to represent an agent, which is "in charge" of choosing the value to assign a given
    variable in the CSP.
    """
    def __init__(self, row, rand_p, least_p, better_p, d_size):
        self.row = row
        self.rand_p = rand_p
        self.least_p = least_p
        self.better_p = better_p
        self.d_size = d_size
        self.x = random.randint(0, d_size - 1) # choose position for agent

    def __repr__(self):
        return "(agent: {}, x: {})".format(self.row, self.x)


    def new_position(self, E):
        """
        Choose agent's next position (i.e. choose the next value in the variable's domain to adopt).
        :param E: Matrix representing the environment.
        """
        r = random.random()

        # Choose a random position with probability rand_p.
        if r <= self.rand_p:
            self.x = random.randint(0, self.d_size - 1)
        # Choose a random position if its violation count is lower with probability better_p.
        elif r <= (self.rand_p + self.least_p):
            new_x = random.randint(0, self.d_size - 1)
            if E[self.row][new_x].violations < E[self.row][self.x].violations:
                self.x = new_x
        # Choose position that minimizes violation count with probability least_p.
        else:
            min_x = 0
            for x in range(self.d_size):
                if E[self.row][x].violations < E[self.row][min_x].violations:
                    min_x = x

            self.x = min_x


class State:
    """
    Class representing a position in the matrix. Stores a single value in the domain of a variable,
    and how many agent positions this value is in violation with.
    """
    def __init__(self, val):
        self.val = val
        self.violations = 0

    def __str__(self):
        return "Val: {val} , Violations: {violations}".format(**self.__dict__)

    def __repr__(self):
        return "({val}, {violations})".format(**self.__dict__)


def attack(p1, p2, E, violation_func):
    """
    Determine whether positions p1 and p2 in the matrix represent values of variables that violate
    a constraint in the CSP.
    :param p1: Pos namedtuple representing the position (x1, y1) in the matrix.
    :param p2: Pos namedtuple representing the position (x2, y2) in the matrix.
    :param E:  The matrix representing the environment.
    :param violation_func: Function for calculating whether a constraint between two
    variables is violated.
    :return: True if E[y1][x1].val and E[y2][x2].val violate a constraint between variables x1 and x2.
             False otherwise.
    """

    return violation_func(p1, p2, E)


def remove_from(x, y, E, violation_func):
    """
    Remove agent from the environment at the coordinates given by x, y.
    :param x: x-coord (column) to add agent to.
    :param y: y-coord (row) to add agent to.
    :param E: Matrix representing the environment.
    :param violation_func: Function for calculating whether a constraint between two
    variables is violated.
    """
    for j in range(len(E)):
        for i in range(len(E[j])):
            if attack(Pos(i, j), Pos(x, y), E, violation_func):
                # Decrement violation count of state at (i, j) and (x, y) represent conflicting values.
                E[j][i].violations = E[j][i].violations - 1
                

def add_to(x, y, E, violation_func):
    """
    Add an agent to the environment at the coordinates given by x, y.
    :param x: x-coord (column) to add agent to.
    :param y: y-coord (row) to add agent to.
    :param E: Matrix representing the environment.
    :param violation_func: Function for calculating whether a constraint between two
    variables is violated.
    """
    for j in range(len(E)):
        for i in range(len(E[j])):
            if attack(Pos(i, j), Pos(x, y), E, violation_func):
                # Increment violation count of state at (i, j) and (x, y) represent conflicting values.
                E[j][i].violations = E[j][i].violations + 1


def get_environment(D):
    """
    Initialize environment given dictionary of variables and their domains.
    :param D: Dictionary of variable numbers to corresponding domains.
    :return:  Matrix of rows representing the environment, list of agents
              corresponding to each variable.
    """
    E = []
    A = []

    for var in D:
        row = []
        for val in D[var]:
            # Add new state corresponding to the given val to the row for the given variable.
            row.append(State(val))

        # Create agent that corresponds to the current variable.
        a_i = Agent(var, RANDOM_P, LEAST_P, BETTER_P, len(D[var]))

        E.append(row)
        A.append(a_i)

    return E, A


def is_solution(E, A):
    """
    Check whether current state of the environment represents a solution to the CSP.
    :param E: Matrix of rows representing the environment.
    :param A: List of agents operating in the environment.
    :return:  True if the current state does represent a solution, False otherwise.
    """

    for a_i in A:
        if E[a_i.row][a_i.x].violations != 0:
            return False

    return True


def print_env(E):
    for row in E:
        print(row)


def era(E, A, violation_func):
    """
    Execute the multi-agent ERA algorithm for solving CSPs.

    :param E: Matrix of rows representing the environment.
    :param A: List of agents operating in the enviornment.
    :param violation_func: Function for determining whether a constraint between two
    variables is violated.
    """

    # Set up initial violation counts by adding agents to their respective positions in the environment.
    for a_i in A:
        add_to(a_i.x, a_i.row, E, violation_func)

    time = 0

    while not is_solution(E, A):
        for a_i in A:
            old_x = a_i.x
            # Have agent select new position.
            a_i.new_position(E)

            if old_x != a_i.x:
                # Decrement violation counts according to removing agent from old_x.
                remove_from(old_x, a_i.row, E, violation_func)
                # Increment violation counts according to adding agent to a_i.x.
                add_to(a_i.x, a_i.row, E, violation_func)

        time += 1