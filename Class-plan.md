# Models - Together - (1 hour)
- [ ] Session
    - [ ] int session_id
    - [ ] List< Session-User > userList
    - [ ] Date startDate
    - [ ] Date endDate
- [ ] Campaign
    - [ ] List< Sessions > sessionList
    - [ ] List< Campaign-User > userList
    - [ ] int campaignid
    - [ ] User gameMaster
    - [ ] int maxPlayers
    - [ ] int currentPlayers
    - [ ] String location
    - [ ] String type
    - [ ] String description
    - [ ] String name
- [ ] User
    - [ ] int userid
    - [ ] String username
    - [ ] String location
    - [ ] String description
    - [ ] Role[Enum] role
    - [ ] List< User-Session > sessionList
    - [ ] List< User-Campaign > campaignList
    - [ ] List< Campaign > hostedCampaignList
- [ ] Session-User
    - [ ] int sessionid
    - [ ] User user
- [ ] User-Session
    - [ ] int userid
    - [ ] Session session
- [ ] Campaign-User
    - [ ] int campaignid
    - [ ] User user
- [ ] User-Campaign
    - [ ] int userid
    - [ ] Campaign campaign
- [ ] AppUser
    - [ ] int appUserId
    - [ ] List< String > roles

# Data
## Mappers
- [ ] Create mappers for each model
## Session -[JULIAN]- (2 hours to write and test)
- [ ] [INTERFACE] Session create()
- [ ] [INTERFACE] boolean update()
- [ ] [INTERFACE] boolean delete(int id)
- [ ] [INTERFACE] Session getFromUserId(int id)
- [ ] [INTERFACE] Session getFromCampaignId(int id)
- [ ] [TEMPLATE] addUsers(User user) -> refer to the bridgemapper
## Campaign -[MUSTAFA]- (4.5 hours to write and test)
- [ ] [INTERFACE] Campaign create()
- [ ] [INTERFACE] boolean update()
- [ ] [INTERFACE] boolean delete(int id)
- [ ] [INTERFACE] Campaign getFromId(int id)
- [ ] [INTERFACE] *Campaign getFromTag(String type, int players, int size, Date start)*
- [ ] [TEMPLATE] addUsers(User user) -> refer to the bridgemapper
- [ ] [TEMPLATE] addSessions(Session session)
## User -[ALEX]- (2 hours to write and test)
- [ ] [INTERFACE] User create()
- [ ] [INTERFACE] boolean update()
- [ ] [INTERFACE] boolean delete(int id)
- [ ] [INTERFACE] User getFromId(int id)
- [ ] [INTERFACE] *User getFromUsername(String name)*
- [ ] [TEMPLATE] addSession(Session session) -> refer to the bridgemapper
- [ ] [TEMPLATE] addCampaign(Campaign campaign) -> refer to the bridgemapper
- [ ] [TEMPLATE] addHostedCampaign(Campaign campaign)
## Session-User -[ALEX]- (1 hour to write and test)
- [ ] [INTERFACE] boolean add(Session-User su)
- [ ] [INTERFACE] boolean update(Session-User su)
- [ ] [INTERFACE] boolean delete(int sessionId, int userId)
## Campaign-User -[ALEX]- (1 hour to write and test)
- [ ] [INTERFACE] boolean add(Campaign-User su)
- [ ] [INTERFACE] boolean update(Campaign-User su)
- [ ] [INTERFACE] boolean delete(int campaignId, int userId)
## AppUser
- [ ] [INTERFACE] AppUser findByUsername(String name)
- [ ] [INTERFACE] AppUser create(AppUser user)
- [ ] [INTERFACE] void update(AppUser user)

-Monday:Data finished-

# Service
## Result < T > -[ALEX]- (0.5 hours)
- [ ] final ArrayList< String > messages
- [ ] ResultType type
- [ ] T payload
- [ ] ResultType getType()
- [ ] boolean isSuccess()
- [ ] T getPayload()
- [ ] void setPayload(T t)
- [ ] List< String > getMessages()
- [ ] void addMessages(String message, ResultType type)
## ResultType [ENUM] -[ALEX]- (0.5 hours)
- [ ] SUCCESS
- [ ] NOT_FOUND
- [ ] INVALID
## SessionService -[JULIAN]- (3 hours)
### Methods
- [ ] Result< Session > create(Session session)
- [ ] Result< Session > update(Session session)
- [ ] boolean delete(int id)
- [ ] Result< Session > getFromUserId(int id)
- [ ] Result< Session > getFromCampaignId(int id)
- [ ] Result< Void > createUser(Session-User su)
- [ ] Result< Void > updateUser(Session-User su)
- [ ] boolean deleteUser(int id)
- [ ] Result< Session > validate(Session session)
### Domain Rules
- Checking for overlap
    1. get sessions based on playerid
    2. get sessions based on campaignid
    3. loop through a nested loop to check for overla
    4. if there is then return INVALID "One or many sessions clash with each other."
- Start must come before an end date
    1. check if start comes before end date
    2. If check fails then return INVALID "Start must come before the end."
- Start and end date must be in the future
    1. check if the start date is after the current
    2. If check failes then return INVALID "The dates must be in the future."
## CampaignService -[MUSTAFA]- (3.5 hours)
### Methods
- [ ] Result< Campaign > create(Campaign campaign)
- [ ] Result< Campaign > update(Campaign campaign)
- [ ] boolean delete(int id)
- [ ] Result< Campaign > getFromUserId(int id)
- [ ] Result< Campaign > getFromCampaignId(int id)
- [ ] Result< Void > createUser(Campaign-User cu)
- [ ] Result< Void > updateUser(Campaign-User cu)
- [ ] boolean deleteUser(int id)
- [ ] Result< Campaign > validate(Campaign campaign)
### Domain Rules
- Prevent negative values for max players
- Prevent player count from going over max players
## UserService -[ALEX]- (2.5 hours)
- [ ] Result< User > create(User user)
- [ ] Result< User > update(User user)
- [ ] *boolean delete(int id)*
- [ ] Result< User > validate(User user)

-Tuesday or Wednesday:Service Finished-

# Controllers
- [ ] Session-User Controller -[ALEX]-
- [ ] Campaign-User Controller -[ALEX]-
- [ ] User Controller -[ALEX]-
- [ ] Campaign Controller -[MUSTAFA]-
- [ ] Session Controller -[JULIAN]-
- [ ] ErrorResponse -[ALEX]-

-Wednesday:Controllers Finished-