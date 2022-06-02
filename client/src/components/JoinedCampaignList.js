import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";
import Errors from "./Errors";

function JoinedCampaignList() {
  const [campaigns, setCampaigns] = useState([]);
  const [targetCampaign, setTargetCampaign] = useState([]);
  const [joinedCampaignIds, setJoinedCampaignIds] = useState([]);
  const [sessionIDs, setSessionIDs] = useState([]);
  const [addSessions, setAddSessions] = useState(false);
  const [leaveSessions, setLeaveSessions] = useState(false);
  const [errors, setErrors] = useState([]);

  const [searchType, setSearchType] = useState('');
  const [searchPlayers, setSearchPlayers] = useState('');
  const [searchSize, setSearchSize] = useState('');
  const [searchCity, setSearchCity] = useState('');
  const [searchState, setSearchState] = useState('');

  const history = useHistory();

  const authManager = useContext(AuthContext);

  const updateCampaignPlayerCount = (tCampaign) => {
    const init = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
        },
        body: JSON.stringify(tCampaign)
    };

      
    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign/${tCampaign.campaignId}`, init)
    .then(response => {
        switch (response.status) {
            case 204:
            return null;
            case 400:
            return response.json();
            case 403:
            authManager.logout();
            history.push('/login');
            break;
            default:
            return Promise.reject('Something went wrong on the server :)');
        }
    })
    .then(body => {
        if (!body) {
            return;
        }
    setErrors(body);
    })
    .catch(err => console.error(err));
  }

  const filterList = (campaignList) => {
    let mutatedCampaignList = campaignList;

    if(searchType !== '') {
      mutatedCampaignList = mutatedCampaignList.filter( (c) => (
        c.type.toLocaleLowerCase() == searchType.toLocaleLowerCase()
      ));
    }

    if(searchPlayers > 0) {
      mutatedCampaignList = mutatedCampaignList.filter( (c) => (
        c.currentPlayers == searchPlayers
      ));
    }

    if(searchSize > 0) {
      mutatedCampaignList = mutatedCampaignList.filter( (c) => (
        c.sessionCount == searchSize
      ));
    }

    if(searchCity !== '') {
      mutatedCampaignList = mutatedCampaignList.filter( (c) => (
        c.city.toLocaleLowerCase() == searchCity.toLocaleLowerCase()
      ));
    }

    if(searchState !== '') {
      mutatedCampaignList = mutatedCampaignList.filter( (c) => (
        c.state.toLocaleLowerCase() == searchState.toLocaleLowerCase()
      ));
    }

    return mutatedCampaignList;
  }
  
  const getJoinedCampaignList = () => {
    return fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign`)
    .then(response => {
        if (response.status ===200) {
            return response.json()
        }
        return Promise.reject('Something went wrong on the server :)');
    })
    .then(body => {
        setJoinedCampaignIds(
            filterList(body).map( (c) => ({
                campaignId:c.campaignId,
                userCampaignList:c.userList.filter((u) => (u.user.userId === authManager.userId))
            }))
            .filter( (c) => c.userCampaignList.length > 0 )
            .filter( (c) => c.userCampaignList[0].user.userId === authManager.userId )
            .map((c) => c.campaignId));
            
        const filteredCampaigns = filterList(body).filter( c => c.userId !== authManager.userId);
        setCampaigns(filteredCampaigns);
    })
    .catch(err => console.error(err));
  }

    useEffect(() => {
        if(addSessions === true) {
          /*
            for(let i = 0;i < sessionIDs.length;i++) {
              addSessionUser(i);
            }
            */

            let falseState = false;
            setAddSessions(falseState);
            
            getJoinedCampaignList();
        }
    }, [addSessions]);

    useEffect(() => {
        if(leaveSessions === true) {
          /*
            for(let i = 0;i < sessionIDs.length;i++) {
              deleteSessionUser(i);
            }
            */

            let falseState = false;
            setLeaveSessions(falseState);
            getJoinedCampaignList();
        }
        }, [leaveSessions]);

    useEffect(() => {
      getJoinedCampaignList();
    }, []);

    const addSessionUser = (id, indexId, tCampaign) => {
        const newSessionUser = {
            sessionId:id,
            user: {
                userId:authManager.userId,
                username: "a",
                city: "a",
                state: "a"
            }
          };
    
          const init = {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
            },
            body: JSON.stringify(newSessionUser)
          };
    
        fetch(`${window.TABLETOPBUDDY_ROOT_URL}/session/user`, init)
        .then(response => {
            if (response.status === 201) {
              return;
            }
            return Promise.reject('Something went wrong on the server :)');
        })
        .catch(err => console.error(err));

        console.log(tCampaign);

        const newUserSchedule = {
          sessionId:id,
          userId:authManager.userId,
          startDate:tCampaign.sessionList[indexId].startDate,
          endDate:tCampaign.sessionList[indexId].endDate
        };

        console.log(newUserSchedule);
  
        const initUS = {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
          },
          body: JSON.stringify(newUserSchedule)
        };
  
        fetch(`${window.TABLETOPBUDDY_ROOT_URL}/userSchedule`, initUS)
        .then(response => {
          if (response.status === 201) {
            return;
          }
          return Promise.reject('Something went wrong on the server :)');
        })
        .catch(err => console.error(err));
    }

    const deleteSessionUser = (id, indexId, tCampaign) => {
      const deleteInit = {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
        },
      };
  
      fetch(`${window.TABLETOPBUDDY_ROOT_URL}/session/user/${id}/${authManager.userId}`, deleteInit)
      .then(response => {
        if (response.status === 204) {
          return;
        }
        return Promise.reject('Something went wrong :)');
      }).catch(err => console.error(err));

      const deleteInitUS = {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
        },
      };
  
      fetch(`${window.TABLETOPBUDDY_ROOT_URL}/userSchedule/su/${id}/${authManager.userId}`, deleteInitUS)
      .then(response => {
        if (response.status === 204) {
          return;
        }
        return Promise.reject('Something went wrong :)');
      }).catch(err => console.error(err));
    }

    const handleGetSessionIDs = (campaign, flag) => {
        const sessionList = campaign.sessionList.map( (s) => ({sessionId:s.sessionId}));
        if(flag){
          for(let i = 0;i < sessionList.length;i++) {
            addSessionUser(sessionList[i].sessionId, i, campaign);
          }
        }else{
          for(let i = 0;i < sessionList.length;i++) {
            deleteSessionUser(sessionList[i].sessionId, i, campaign);
          }
        }

        updateCampaignPlayerCount(campaign);

        setSessionIDs(sessionList);
    }

    const checkForValidJoin = (campaign) => {
      let sessionList = campaign.sessionList;

      return fetch(`${window.TABLETOPBUDDY_ROOT_URL}/userSchedule/${authManager.userId}`)
      .then(response => {
          if (response.status ===200) {
              return response.json()
          }
          return Promise.reject('Something went wrong on the server :)');
      })
      .then(body => {
        for(let j = 0;j < body.length;j++) {
          for(let i = 0;i < sessionList.length;i++) {
            console.log(body[j].startDate);
            console.log(sessionList[i].startDate);

            if((body[j].startDate<(sessionList[i].endDate) && body[j].startDate>(sessionList[i].startDate))
            || (body[j].endDate<(sessionList[i].endDate) && body[j].endDate>(sessionList[i].startDate))
            || (body[j].startDate<(sessionList[i].startDate) && body[j].endDate>(sessionList[i].endDate))
            || body[j].startDate === (sessionList[i].endDate) || body[j].startDate === (sessionList[i].startDate)
            || body[j].endDate === (sessionList[i].endDate) || body[j].endDate === (sessionList[i].startDate)) {
              setErrors(['User has a schedule that does not fit.'])
              return false;
            }
          }
        }
        return true;
      })
      .catch(err => console.error(err));
    }

  const handleJoinSelect = (campaign) => {
    checkForValidJoin(campaign).then( (result) => {
      console.log(campaign.currentPlayers);
      console.log(campaign.maxPlayers);
      console.log(result);
      if(result){
        if(campaign.currentPlayers < campaign.maxPlayers){
        const newCampaignUser = {
            campaignId:campaign.campaignId,
            user: {
                userId:authManager.userId,
                username: "a",
                city: "a",
                state: "a"
            }
          };

          const init = {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
            },
            body: JSON.stringify(newCampaignUser)
          };


      fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign/user`, init)
      .then(response => {
          if (response.status === 201) {
              handleGetSessionIDs(campaign, true);
              setTargetCampaign(campaign);
              setAddSessions(true);
              return;
          }
          return Promise.reject('Something went wrong on the server :)');
      })
      .catch(err => console.error(err));
    }else{
      setErrors(['The campaign is full']);
    }
  }
  })
  }  

  const handleLeaveSelect =(campaign) => {
    const deleteInit = {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
        },
      };

      fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign/user/${campaign.campaignId}/${authManager.userId}`, deleteInit)
    .then(response => {
      if (response.status === 204) {
        handleGetSessionIDs(campaign, false);
        setTargetCampaign(campaign);
        setLeaveSessions(true);
        return;
      } else if (response.status === 400) {
        return response.json();
      }
      else if (response.status === 403) {
        authManager.logout();
        history.push('/login');
      }

      return Promise.reject('Something went wrong :)');
    })
    .then(body => {
      if (!body) {
        return;
      }

      setErrors(body);
    })
    .catch(err => console.error(err));
  }

  const handleViewSelect = (campaign) => {
    history.push(`/campaign/view/${campaign.campaignId}`);
  }

  const handleType = (event) => {
    setSearchType(event.target.value);
  } 

  const handlePlayerCount = (event) => {
    setSearchPlayers(event.target.value);
  } 

  const handleSessionSize = (event) => {
    setSearchSize(event.target.value);
  }

  const handleCity = (event) => {
    setSearchCity(event.target.value);
  }

  const handleState = (event) => {
    setSearchState(event.target.value);
  }

  const handleSearch = (event) => {
    event.preventDefault();
    getJoinedCampaignList();
  }

  return (
    <>
      <div>
        <h2 className="mt-5">Campaign List</h2>
        <form>
          <div className="container">
            <div className ="row">
            <div className ="col-sm">
            <label htmlFor="type">Type:</label>
            &nbsp;
            <input className="form-control" type="text" id="type" name="type" value={searchType} onChange={handleType} ></input>
            </div>
            <div className ="col-sm">
            <label htmlFor="players">Player Count:</label>
            &nbsp;
            <input className="form-control col-xs-2" type="number" id="playerCount" name="playerCount" value={searchPlayers} onChange={handlePlayerCount} ></input>
            </div>
            <div className ="col-sm">
            <label htmlFor="size">Session Count:</label>
            &nbsp;
            <input className="form-control col-xs-2" type="number" id="size" name="size" value={searchSize} onChange={handleSessionSize} ></input>
            </div>
            <div className ="col-sm">
            <label htmlFor="size">City:</label>
            &nbsp;
            <input className="form-control col-xs-2" type="text" id="size" name="size" value={searchCity} onChange={handleCity} ></input>
            </div>
            <div className ="col-sm">
            <label htmlFor="size">State:</label>
            &nbsp;
            <input className="form-control col-xs-2" type="text" id="size" name="size" value={searchState} onChange={handleState} ></input>
            </div>
            <div className ="col-sm">
            <button className="btn btn-primary" type="search" onClick={handleSearch}>Filter</button>
            </div>
            </div>
          </div>
        </form>
        &nbsp;
        <table className="table table-sm">
          <thead>
            <tr>
                <th scope="col"></th>
                <th scope="col">#</th>
                <th scope="col">Name</th>
                <th scope="col">Type</th>
                <th scope="col">City</th>
                <th scope="col">State</th>
                <th scope="col">Session Count</th>
                <th scope="col">Player Count</th>
            </tr>
          </thead>
          <tbody>
            {campaigns.map((cmp, i) => (
              <tr key={cmp.campaignId}>
                <td>
                    {authManager.user ? (<>
                    {joinedCampaignIds.includes(cmp.campaignId) ? 
                    <button className="btn btn-secondary" type="button" onClick={() => handleLeaveSelect(cmp)} >Leave</button>
                    :
                    <button className="btn btn-info" type="button" onClick={() => handleJoinSelect(cmp)} >Join</button>}
                    </>) : null}
                    &nbsp;
                    <button className="btn btn-info" type="button" onClick={() => handleViewSelect(cmp)} >View</button>
                </td>
                <td>
                  &nbsp;
                  {i + 1}
                </td>
                <td>
                    {cmp.name}
                  </td>
                  <td>
                    {cmp.type}
                  </td>
                  <td>
                    {cmp.city}
                  </td>
                  <td>
                    {cmp.state}
                  </td>
                  <td>
                    {cmp.sessionCount}
                  </td>
                  <td>
                    {cmp.currentPlayers + '/' + cmp.maxPlayers}
                  </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      &nbsp;
      <Errors errors={errors}/>
    </>
  )
}

export default JoinedCampaignList;