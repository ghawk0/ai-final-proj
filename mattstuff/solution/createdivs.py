import pickle

def create_divisions(player_dict, div_sizes):

    player_by_rating = {}

    for player in player_dict:
        player_tuple = (player, player_dict[player])
        rating = player_tuple[1][3]

        if rating in player_by_rating:
            player_by_rating[rating].append(player_tuple)
        else:
            rating_list = []
            rating_list.append(player_tuple)
            player_by_rating[rating] = rating_list

    ordered_players = []
    for rating in sorted(player_by_rating.keys()):
        for player_tuple in player_by_rating[rating]:
            ordered_players.append(player_tuple)

    player_dict_by_division = {}

    count = 0

    for div in range(len(div_sizes)):
        player_dict_by_division[div] = {}

        for i in range(div_sizes[div]):
            player_num, player_info = ordered_players[count]
            player_dict_by_division[div][player_num] = player_info
            count += 1

    return player_dict_by_division

def main():

    # Number of teams per division
    div_teams = []
    # Number of players per division
    div_sizes = []

    # Get number of divisions
    with open('div-info.txt', 'r+') as f:
        k = int(f.readline())

        for line in f:
            num_teams, team_size = (line.rstrip().split(','))
            num_players = int(num_teams) * int(team_size)
            div_teams.append(int(num_teams))
            div_sizes.append(num_players)

    with open('player_info.pickle', 'rb') as f:
        player_dict = pickle.load(f)

    player_dict_by_division = create_divisions(player_dict, div_sizes)

    # Loop through divisions and create .dat files to represent them
    for div in range(len(div_sizes)):

        varnum_to_playernum = {}

        filename = "compdata{}.dat".format(div)
        pickle_file = "var-player-map-div-{}.pickle".format(div)

        with open(filename, "w+") as f:
            # Print out # of teams and # of players on the given division
            f.write("param m := {num_teams}\n;".format(num_teams = div_teams[div]))
            f.write("param n := {div_size}\n;".format(div_size = div_sizes[div]))

            sum_ratings = 0

            for player_num in player_dict_by_division[div]:
                player_info = player_dict_by_division[div][player_num]
                sum_ratings += player_info[3]

            # target = sum of ratings divided by number of teams.
            target_rating = sum_ratings // div_teams[div]
            f.write("param t := {target}\n;".format(target=target_rating))

            p = div_sizes[div] // div_teams[div]
            f.write("param p := {}\n;".format(p))
            f.write("param x :=\n")

            count = 1
            for player_num in player_dict_by_division[div]:
                player_info = player_dict_by_division[div][player_num]
                f.write("{var_num} {rating}\n".format(var_num=count, rating = player_info[3]))
                varnum_to_playernum[count] = player_num
                count += 1

            f.write(";")

            with open(pickle_file, 'wb') as g:
                pickle.dump(varnum_to_playernum, g)



main()