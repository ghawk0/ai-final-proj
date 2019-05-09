# round robin algorithm for getting pairings of teams
def robin(teams, matchups=None):
    if len(teams) % 2:
        teams.append(None)
    count = len(teams)
    matchups = matchups or (count-1)
    half = count/2
    schedule = []
    for round in range(matchups):
        pairs = []
        for i in range(half):
            if (teams[i] is None) or (teams[count-i-1] is None):
                none = True
            else:
                pairs.append(teams[i], teams[count-i-1])
        teams.insert(1,teams.pop())
        schedule.append(pairs)
    return schedule

if __name__=='__main__':
    for pairings in robin(range(5)):
        print(pairings)

