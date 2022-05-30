import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";

function JoinedCampaignList() {

  const [campaigns, setCampaigns] = useState([]);

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
            const filteredCampaignIdsWithUserIds = body.map( (c) => 
                ({
                    campaignId:c.campaignId,
                    userCampaignList:c.userList.filter((u) => (u.user.userId === authManager.userId))
                })
            );
            const filteredCampaignsIds = filteredCampaignIdsWithUserIds.filter( (c) => c.userCampaignList.length > 0 ).filter( (c) => c.userCampaignList[0].user.userId === authManager.userId );
            const filteredCampaignsIdsList = filteredCampaignsIds.map((c) => c.campaignId);
            const filteredCampaigns = body.filter( (c) => (filteredCampaignsIdsList.includes(c.campaignId)) );
            */

            const filteredCampaigns = body.filter( (c) => (
                body.map( (c) => ({
                    campaignId:c.campaignId,
                    userCampaignList:c.userList.filter((u) => (u.user.userId === authManager.userId))
                }))
                .filter( (c) => c.userCampaignList.length > 0 )
                .filter( (c) => c.userCampaignList[0].user.userId === authManager.userId )
                .map((c) => c.campaignId)
                .includes(c.campaignId)) );
            setCampaigns(filteredCampaigns);
        })
        .catch(err => console.error(err));
  }


  useEffect(() => {
    getJoinedCampaignList();
    }, []);


  const handleJoinSelect = () => {
      history.push(`/campaign/add`);
  }  

  const handleLeaveSelect =(campaign) => {
      history.push(`/campaign/edit/${campaign.campaignId}`);
  }

  const handleDeleteSelect = (campaign) => {
      history.push(`/campaign/delete/${campaign.campaignId}`);
  }

  return (
    <>
      <div>
        <h2 className="mt-5">Joined Campaign List</h2>
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