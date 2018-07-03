package com.example.serkanaysan.livescore


//Main
class MainFeed(val leagues: List<League>)
class League(val country_id: String, val country_name: String, val league_id: String, val league_name: String)

//Live
class LiveFeed(val matchs: List<Match>)
class Match(val match_id: String, val country_id: String, val country_name: String, val league_id: String,
               val league_name: String, val match_date: String, val match_status: String, val match_time: String,
               val match_hometeam_name: String, val match_hometeam_score: String, val match_awayteam_name: String,
               val match_awayteam_score: String, val match_hometeam_halftime_score: String, val match_awayteam_halftime_score: String,
               val match_hometeam_system: String, val match_awayteam_system: String, val match_live: String, val goalscorer: List<GoalScorer>,
               val cards: List<Cards>)
class GoalScorer(val time: String, val home_scorer: String, val score: String, val away_scorer: String)
class Cards(val time: String, val home_fault: String, val card: String, val away_fault: String)
class LineUp(val home: LineUpDetail, val away: LineUpDetail)

class LineUpDetail(val starting_lineups: List<String>, val substitutes: List<String>, val coach: List<String>, val substitutions: String)

class OddFeed(val odds: List<Odd>)
class Odd(val match_id: String, val odd_bookmakers: String, val odd_date: String, val odd_1: String, val odd_x: String, val odd_2: String)

class HomeTeamFeed(val firstTeam_lastResults: List<Match>)
class AwayTeamFeed(val secondTeam_lastResults: List<Match>)
class H2HFeed(val firstTeam_VS_secondTeam: List<Match>)