import yaml
import pickle
import sys

teams = {}

divnumber = sys.argv[1]

with open("results.yml", 'r') as stream:
        
        sony = []
        data = yaml.safe_load(stream)
        #print(data['Solution'][1]['Variable'])
        for i in data['Solution'][1]['Variable']:
            if i[0] == 'y':
                index = i.find(',')
                sony.append(int(i[2:index]))
                
        for i in range(1,max(sony)+1):
            teams[i] = []        
                
        for i in data['Solution'][1]['Variable']:
            if i[0] == 'y':
                index = i.find(',')
                teams[int(i[2:index])].append(i[index+1:-1])
              
              
                

  
with open('player_info.pickle', 'rb') as f:
    player_dict = pickle.load(f)
    

filename = "var-player-map-div-{}".format(divnumber)

with open(filename, 'rb') as f:
    div1_dict = pickle.load(f)



for i in teams:
    for j in range(len(teams[i])):
        teams[i][j] = div1_dict[int(teams[i][j])]
       
print(teams)
        
file1 = open("teams.txt","w")
m = len(teams)
for i in range(1,m+1):
    file1.write("Team " + str(i) + "\n")
    for j in teams[i]:
        file1.write(str(player_dict[int(j)][0])+ ", " + str(player_dict[int(j)][1])+ ", " + str(player_dict[int(j)][2])+"\n")
    file1.write("\n" )

print("teams.txt file generated")