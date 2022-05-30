import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";
import Errors from "./Errors";

function JoinedCampaignList() {
  const [campaigns, setCampaigns] = useState([]);
  const [joinedCampaignIds, setJoinedCampaignIds] = useState([]);
  const [errors, setErrors] = useState([]);

  const history = useHistory();

  const authManager = useContext(AuthContext);
  
  const getJoinedCampaignList = () => {
    return fetch('http://localhost:8080/api/campaign')
    .then(response => {
        if (response.status ===200) {
            return response.json()
        }
        return Promise.reject('Something went wrong on the server :)');
    })
    .then(body => {
        /*
        const filteredCampaigns = body.filter( (c) => (
            body.map( (c) => ({
                campaignId:c.campaignId,
                userCampaignList:c.userList.filter((u) => (u.user.userId === authManager.userId))
            }))
            .filter( (c) => c.userCampaignList.length > 0 )
            .filter( (c) => c.userCampaignList[0].user.userId === authManager.userId )
            .map((c) => c.campaignId)
            .includes(c.campaignId)) );
            */

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
        getJoinedCampaignList();
    }, []);


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

    fetch('http://localhost:8080/api/campaign/user', init)
    .then(response => {
        if (response.status === 201) {
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

      fetch(`http://localhost:8080/api/campaign/user/${campaign.campaignId}/${authManager.userId}`, deleteInit)
    .then(response => {
      if (response.status === 204) {
        history.go(0);
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
                <th scope="col">Next Session</th>
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
                    {cmp.nextSession}
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