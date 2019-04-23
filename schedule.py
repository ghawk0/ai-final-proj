class team():
    def __init__(self, num, players, matchups):
        self.num = num
        self.players = players
        self.matchups = matchups
        
class player():
    def __init__(self, name, gen, pgen):
        self.name = name
        self.gen = gen
        self.gen = pgen
        
def clean(list):
    newlist = []
    for i in list:
        if i.endswith(','):
            i = i[:-1]
        if i.isdigit():
            i = int(i)
        newlist.append(i)
    return newlist

def addteam(list):
    teamnum = list[0] + str(list[1])
    players = []
    matchups = []
    for i in range(2, len(list), 3):
        if list[i] == int:
            for j in range(i, len(list)):
                matchups.append(list[j])
            break
        else:
            list[i] = player(list[i], list[i+1], list[i+2])
            players.append(list[i])
            i = i + 2
    teamnum = (teamnum, players, matchups)
    return teamnum
          
teams = []
filename = 'teams.txt'
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
    
print(teams[0][1])