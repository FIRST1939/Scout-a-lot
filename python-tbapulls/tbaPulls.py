# -*- coding: utf-8 -*-
"""
Created on Wed Feb 25 15:23:30 2026

@author: vhcook

Intent is to pull TBA match data and use it to generate useful data.
"""

#TODO Port over TBAUtils get for match data
import urllib.request
import json
from time import sleep
from datetime import date

URL = 'http://www.thebluealliance.com/api/v3/'

THISYEAR = date.today().year

with open("../androidApp/src/main/assets/X-TBA-Auth-Key.txt") as f:
    tbakey = f.readlines()[0]


REQHEADERS = {'X-TBA-Auth-Key': tbakey,
              'User-Agent': 'Mozilla/5.0'}

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
      {
        "key": "string",
        "comp_level": "qm",
        "set_number": 0,
        "match_number": 0,
        "alliances": {
          "red": {
            "score": 0,
            "team_keys": [
              "string"
            ],
            "surrogate_team_keys": [
              "string"
            ],
            "dq_team_keys": [
              "string"
            ]
          },
          "blue": {
            "score": 0,
            "team_keys": [
              "string"
            ],
            "surrogate_team_keys": [
              "string"
            ],
            "dq_team_keys": [
              "string"
            ]
          }
        },
        "winning_alliance": "red",
        "event_key": "string",
        "time": 0,
        "actual_time": 0,
        "predicted_time": 0,
        "post_result_time": 0,
        "score_breakdown": {},
        "videos": [
          {
            "type": "string",
            "key": "string"
          }
        ]
      }
    ]
    '''
    event_key = str(year) + event
    fullurl = URL + 'event/' + event_key + '/matches'
    print(fullurl)
    result = get_request(fullurl)
    return result
#TODO Analyze Auton Data
#TODO Auto OPRs?

