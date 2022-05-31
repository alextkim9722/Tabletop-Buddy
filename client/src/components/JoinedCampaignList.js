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
  const [searchStart, setSearcStart] = useState('');

  const history = useHistory();

  const authManager = useContext(AuthContext);

  const updateCampaignPlayerCount = () => {
    const init = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
        },
        body: JSON.stringify(targetCampaign)
    };

      
    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign/${targetCampaign.campaignId}`, init)
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
            body.map( (c) => ({
                campaignId:c.campaignId,
                userCampaignList:c.userList.filter((u) => (u.user.userId === authManager.userId))
            }))
            .filter( (c) => c.userCampaignList.length > 0 )
            .filter( (c) => c.userCampaignList[0].user.userId === authManager.userId )
            .map((c) => c.campaignId));
            
        const filteredCampaigns = body.filter( c => c.userId !== authManager.userId);
        setCampaigns(filteredCampaigns);
    })
    .catch(err => console.error(err));
  }

    useEffect(() => {
        if(addSessions === true) {
            for(let i = 0;i < sessionIDs.length;i++) {
                addSessionUser(sessionIDs[i].sessionId);
                setAddSessions(false);
            }

            updateCampaignPlayerCount();
        }
    }, [addSessions]);

    useEffect(() => {
        if(leaveSessions === true) {
            for(let i = 0;i < sessionIDs.length;i++) {
                deleteSessionUser(sessionIDs[i].sessionId);
                setLeaveSessions(false);
            }

            updateCampaignPlayerCount();
        }
        }, [leaveSessions]);

    useEffect(() => {
        getJoinedCampaignList();
    }, []);

    const addSessionUser = (id) => {
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
    }

    const deleteSessionUser = (id) => {
        const deleteInit = {
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
            },
          };
    
          fetch(`${window.TABLETOPBUDDY_ROOT_URL}/session/user/${id}/${authManager.userId}`, deleteInit)
        .then(response => {
          if (response.status === 204) {
              setLeaveSessions(false);
              history.go(0);
            return;
          }
          return Promise.reject('Something went wrong :)');
        }).catch(err => console.error(err));
    }

    const handleGetSessionIDs = (campaign) => {
        const sessionList = campaign.sessionList.map( (s) => ({sessionId:s.sessionId}));
        setSessionIDs(sessionList);
        console.log(sessionList);
    }

  const handleJoinSelect = (campaign) => {
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
            handleGetSessionIDs(campaign);
            setTargetCampaign(campaign);
            setAddSessions(true);
            history.go(0);
            return;
        }
        return Promise.reject('Something went wrong on the server :)');
    })
    .catch(err => console.error(err));
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
        handleGetSessionIDs(campaign);
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

  return (
    <>
      <div>
        <h2 className="mt-5">Campaign List</h2>
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
    </>
  )
}

export default JoinedCampaignList;