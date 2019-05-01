import sys

rankings = {}

"""
matchups = list of best to worst of the other teams to play against
players = list of players on the team
num = team #
"""

class team():
    def __init__(self, num, players, matchups):
        self.num = num
        self.players = players
        self.matchups = matchups

    def __str__(self):
        return str(self.num)

    def __repr__(self):
        return str(self.num)

"""
name = string name
gen = player's gender 
pgen = preferred gender to match up against (M, F, NB=nonbinary, A = anyone)
"""
class player():
    def __init__(self, name, gen, pgen):
        self.name = name
        self.gen = gen
        self.pgen = pgen

    def __str__(self):
        return str(self.name)

    def __repr__(self):
        return str(self.name)

"""
input is all the messy strings, in a list, that represent a single team
the class cleans up that input, converting string numbers into int numbers, removing commas
"""
def clean(list):
    newlist = []
    for i in list:
        if i.endswith(','):
            i = i[:-1]
        if i.isdigit():
            i = int(i)
        newlist.append(i)
    return newlist

"""
takes the clean list (representing a team) 
and builds an instance of the team class from it
goes down the list in threes, takes a player and adds their pgens
"""
def addteam(list):
    teamnum = list[0] + str(list[1])
    players = []
    matchups = []
    for i in range(2, len(list), 3): #starting to parse through the list
        if list[i] == int: #already passed the "team 1" headline, so if we find another integer it's the matchup preferences
            for j in range(i, len(list)): #so we loop on the rest of the list and add each to the list of matchups
                matchups.append(list[j])
            break
        else:
            list[i] = player(list[i], list[i+1], list[i+2])
            players.append(list[i])
            i = i + 2
    teamnum = team(teamnum, players, matchups)
    return teamnum


def computeScore(team1, team2):
    score = 0
    used = []
    for player1 in team1.players:
        for player2 in team2.players:
            if player2 in used:
                continue
            elif player1.pgen == 'A':
                score += 1
                break
            elif player1.pgen == player2.gen:
                score += 1
                used.append(player2)
                break
    # once this for loop has done its thing, we will have updated the score for how good
    # team1 feels about playing team 2, now we need to figure out how good team2 feels
    # about playing team1

    used2 = []
    for player2 in team2.players:
        for player1 in team1.players:
            if player1 in used2:
                continue
            elif player2.pgen == 'A':
                score += 1
                break
            elif player2.pgen == player1.gen:
                score += 1
                used2.append(player1)
                break
    return score


def rankTeams(teams):
    for team1 in teams:
        for team2 in teams:
            score = computeScore(team1, team2)
            rankings[team1, team2] = score
            rankings[team2, team1] = score


def main():
    teams = []
    pots = sys.argv[1]
    filename = sys.argv[2]
    # let's maybe have the first argument be the number of pots
    with open(filename) as f:
        teamlist = []
        for line in f:
            for word in line.split():
                if word == "Team":
                    newteam = True
                    if teamlist:
                        #print(clean(teamlist))
                        teams.append(addteam(clean(teamlist)))
                    teamlist = []
                if newteam:
                    teamlist.append(word)
        #print(clean(teamlist))
        teams.append(addteam(clean(teamlist)))
    print(teams[0].players[0].name)
    rankTeams(teams)
    print(rankings)


if __name__ == '__main__':
    main()




