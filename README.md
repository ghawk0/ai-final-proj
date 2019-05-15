# Optimal Ultimate Pickup System

## File Structure

## How to Run System
Note that simply running the scipt ```runsystem.py``` will execute all the following steps automatically,
generating sample input and finding a final assignment of players to teams, assignment of divisions to fields,
and playing schedule. To execute this process manually, and to illustrate each step in this system, we 
provide the following steps: 

1. Generate sample input: 
    * Generate sample input by running ``` ai-final-proj/solution/inputgenerator.py ```. This creates
    
        1. File ```div-input.txt```. The format of this file is specified in ```ai-final-proj/sample-files/div-input-format.txt```. 
        2. The start to file ```field-input.txt```. The format of this file is specified in ```ai-final-proj/sample-files/field-input-format.txt```. 
        3. File ```player_info.pickle``` containing a pickle dump of a dictionary representing the players in the problem. The dictionary is a map to player number to a list of player info, where the info is in the format ```[Name, Gender, Preferred Gender Matchup, Competitiveness Rating]```
        
2. Create division information: 
   * Run ```ai-final-proj/team-assignment/bin/CreateDivisions div-input.txt```. This generates ```ai-final-proj/solution/div-info.txt```. This file prints out the number of divisions needed on the first line. Each subsequent line represents a divsion and has the form ```t,p```, where t is the number of teams on the division and p is the number of players per team on that division.  
   * Run ```ai-final-proj/solution/creatdivs.py```. This reads in the dictionary of players in ```player_info.dict``` and divides them into the number of divisions specified in ```div-info.txt```. This is done in order of lowest player rating to highest, such that players are placed in division according to ability. Based on this, the following outputs are generated:
   
      1. The .dat files necessary for running the pyomo linear optimization programs. A .dat file is generated per division. Each such file has name ```compdata{div#}.dat```. The format of these .dat files is specified in ```ai-final-proj/sample-files/compdata-format.dat```.
      2. Since the pyomo uses new variable numbers to represent each player, we need to create dictionaries to map these new variable numbers to the original player numbers we had. As such, for each division, we output file ```var-player-map-div-{div#}.pickle```. This stores a pickled dictionary of ```new-var-number -> original-player-number``` so we can recover information after the linear program finishes running. 

3. 
