from pyomo.environ import *
import random

random.seed(1000)


model = AbstractModel()


#total number of players in this div
model.n = Param(within=PositiveIntegers)

#total number of teams in this div
model.m = Param(within=PositiveIntegers)

# Number of players per team in this div
model.p = Param(within=PositiveIntegers)
#target rating for each team
model.t = Param(within=PositiveIntegers)
#defining a set I to be the set of teams
model.I = RangeSet(1, model.m)

#defining a set J to be the full set of players
model.J = RangeSet(1, model.n)

#rating for each player
model.x = Param(model.J)
#Yij = 1 iff player j is on team i
model.y = Var(model.I, model.J, within=Binary)
#neg-error for each team
model.d = Var(model.I, domain=NonNegativeReals)
#pos-error for each team
model.e = Var(model.I, domain=NonNegativeReals)

def obj_expression(model):
    return summation(model.d) 

model.OBJ = Objective(rule=obj_expression)

def ax_constraint_rule(model, i):

    return sum(model.y[i,j] for j in model.J) == model.p

model.AxbConstraint = Constraint(model.I, rule=ax_constraint_rule)

def negerror(model, i):
    # return the expression for the constraint for i
    return sum(model.x[j]*model.y[i,j] for j in model.J) + model.d[i] - model.e[i] == model.t


model.NegError = Constraint(model.I, rule=negerror)

# def poserror(model, i):
#     # return the expression for the constraint for i
#     return sum(model.x[j]*model.y[i,j] for j in model.J) + model.d[i] == model.t
# 
# 
# model.PosError = Constraint(model.I, rule=poserror)

def eachplayer(model, j):

    return sum(model.y[i,j] for i in model.I) == 1

model.EachPlayer = Constraint(model.J, rule=eachplayer)


