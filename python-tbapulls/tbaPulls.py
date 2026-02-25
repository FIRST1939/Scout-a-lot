# -*- coding: utf-8 -*-
"""
Created on Wed Feb 25 15:23:30 2026

@author: vhcook

Intent is to pull TBA match data and use it to generate useful data.
"""
import urllib.request
import json
from time import sleep
from datetime import date
from pprint import pprint

URL = 'http://www.thebluealliance.com/api/v3/'

THISYEAR = date.today().year

with open("../androidApp/src/main/assets/X-TBA-Auth-Key.txt") as f:
    tbakey = f.readlines()[0]


REQHEADERS = {'X-TBA-Auth-Key': tbakey, 'User-Agent': 'Mozilla/5.0'}

def get_request(fullurl):
    request = urllib.request.Request(fullurl, headers=REQHEADERS)
    response = urllib.request.urlopen(request)
    jsonified = json.loads(response.read().decode("utf-8"))
    return jsonified

def get_event_matches(event, year=THISYEAR):
    '''

    @param event:
    @param year:
    @return: schema
    [
        {'actual_time': 1771694253,
         'alliances': {'blue': {'dq_team_keys': [],
                                'score': 47,
                                'surrogate_team_keys': [],
                                'team_keys': ['frc3566', 'frc1073', 'frc8724']},
                       'red': {'dq_team_keys': [],
                               'score': 28,
                               'surrogate_team_keys': [],
                               'team_keys': ['frc2342', 'frc131', 'frc811']}},
         'comp_level': 'qm',
         'event_key': '2026week0',
         'key': '2026week0_qm13',
         'match_number': 13,
         'post_result_time': 1771694469,
         'predicted_time': 1771694073,
         'score_breakdown': {'blue': {'adjustPoints': 0,
                                      'autoTowerPoints': 0,
                                      'autoTowerRobot1': 'None',
                                      'autoTowerRobot2': 'None',
                                      'autoTowerRobot3': 'None',
                                      'endGameTowerPoints': 0,
                                      'endGameTowerRobot1': 'None',
                                      'endGameTowerRobot2': 'None',
                                      'endGameTowerRobot3': 'None',
                                      'energizedAchieved': False,
                                      'foulPoints': 0,
                                      'g206Penalty': False,
                                      'hubScore': {'autoCount': 3,
                                                   'autoPoints': 3,
                                                   'endgameCount': 9,
                                                   'endgamePoints': 9,
                                                   'shift1Count': 20,
                                                   'shift1Points': 20,
                                                   'shift2Count': 0,
                                                   'shift2Points': 0,
                                                   'shift3Count': 14,
                                                   'shift3Points': 14,
                                                   'shift4Count': 0,
                                                   'shift4Points': 0,
                                                   'teleopCount': 44,
                                                   'teleopPoints': 44,
                                                   'totalCount': 47,
                                                   'totalPoints': 47,
                                                   'transitionCount': 1,
                                                   'transitionPoints': 1,
                                                   'uncounted': 0},
                                      'majorFoulCount': 0,
                                      'minorFoulCount': 0,
                                      'penalties': 'None',
                                      'rp': 3,
                                      'superchargedAchieved': False,
                                      'totalAutoPoints': 3,
                                      'totalPoints': 47,
                                      'totalTeleopPoints': 44,
                                      'totalTowerPoints': 0,
                                      'traversalAchieved': False},
                             'red': {'adjustPoints': 0,
                                     'autoTowerPoints': 0,
                                     'autoTowerRobot1': 'None',
                                     'autoTowerRobot2': 'None',
                                     'autoTowerRobot3': 'None',
                                     'endGameTowerPoints': 10,
                                     'endGameTowerRobot1': 'None',
                                     'endGameTowerRobot2': 'Level1',
                                     'endGameTowerRobot3': 'None',
                                     'energizedAchieved': False,
                                     'foulPoints': 0,
                                     'g206Penalty': False,
                                     'hubScore': {'autoCount': 7,
                                                  'autoPoints': 7,
                                                  'endgameCount': 0,
                                                  'endgamePoints': 0,
                                                  'shift1Count': 0,
                                                  'shift1Points': 0,
                                                  'shift2Count': 2,
                                                  'shift2Points': 2,
                                                  'shift3Count': 0,
                                                  'shift3Points': 0,
                                                  'shift4Count': 2,
                                                  'shift4Points': 2,
                                                  'teleopCount': 11,
                                                  'teleopPoints': 11,
                                                  'totalCount': 18,
                                                  'totalPoints': 18,
                                                  'transitionCount': 7,
                                                  'transitionPoints': 7,
                                                  'uncounted': 0},
                                     'majorFoulCount': 0,
                                     'minorFoulCount': 0,
                                     'penalties': 'None',
                                     'rp': 0,
                                     'superchargedAchieved': False,
                                     'totalAutoPoints': 7,
                                     'totalPoints': 28,
                                     'totalTeleopPoints': 21,
                                     'totalTowerPoints': 10,
                                     'traversalAchieved': False}},
         'set_number': 1,
         'time': 1771691760,
         'videos': [],
         'winning_alliance': 'blue'}
    '''
    event_key = str(year) + event
    fullurl = URL + 'event/' + event_key + '/matches'
    print(fullurl)
    result = get_request(fullurl)
    return result

def getautons(event, year=THISYEAR):
    matchlist = get_event_matches(event, year)
    if matchlist == []:
        return #Event hasn't sent any data yet
    
    result = []
    for match in matchlist:
        #quals match that has played
        if match['comp_level'] == 'qm' and match['actual_time'] > 0: 
            interesting = [match['key'], match['alliances']['blue']['team_keys'], 
                           match['alliances']['red']['team_keys'],
                           match['score_breakdown']['blue']['hubScore']['autoPoints'], 
                           match['score_breakdown']['red']['hubScore']['autoPoints']]
            
            result.append(interesting)
    pprint(result)            
    #return result
    
        
#TODO Analyze Auton Data
#TODO Auto OPRs?

