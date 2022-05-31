import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";

function HostedCampaignList() {

    const [campaigns, setCampaigns] = useState([]);
    const history = useHistory();

    const authManager = useContext(AuthContext);

    let getHostedCampaigns = () => {
        return fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign`)
        .then(response => {
            if (response.status ===200) {
                return response.json()
            }
            return Promise.reject('Something went wrong on the server :)');
        })
        .then(body => {
            const filteredCampaigns = body.filter( c => c.userId === authManager.userId);
            setCampaigns(filteredCampaigns);
        })
        .catch(err => console.error(err));
    }

    useEffect(() => {
        getHostedCampaigns();
    }, []);

    const handleAddSelect = () => {
        history.push(`/campaign/add`);
    }  

    const handleEditSelect =(campaign) => {
        history.push(`/campaign/edit/${campaign.campaignId}`);
    }

    const handleDeleteSelect = (campaign) => {
        history.push(`/campaign/delete/${campaign.campaignId}`);
    }

    const handleViewSelect = (campaign) => {
        history.push(`/campaign/view/${campaign.campaignId}`);
      }

    return (
    <>
        <div>
        <h2 className="mt-5">Hosted Campaign List</h2>
        {authManager.user ? <button className="btn btn-primary mb-3 mt-4" type="button" onClick={handleAddSelect}>Add Campaign</button> : null}
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
                    <button className="btn btn-info" type="button" onClick={() => handleEditSelect(cmp)} >Edit</button>
                    &nbsp;
                    <button className="btn btn-secondary" type="button" onClick={() => handleDeleteSelect(cmp)} >Delete</button>
                    <button className="btn btn-info" type="button" onClick={() => handleViewSelect(cmp)} >View</button>
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

export default HostedCampaignList;