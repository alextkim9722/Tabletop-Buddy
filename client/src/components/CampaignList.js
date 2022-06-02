import HostedCampaignList from "./HostedCampaignList";
import JoinedCampaignList from "./JoinedCampaignList";
import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";

function Campaigns() {

  const [campaigns, setCampaigns] = useState([]);

  const history = useHistory();

  const authManager = useContext(AuthContext);
  
  let getCampaigns = () => {
    return fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign`)
    .then(response => {
      if (response.status ===200) {
        return response.json()
      }
      return Promise.reject('Something went wrong on the server :)');
    })
    .then(body => {
      setCampaigns(body);
    })
    .catch(err => console.error(err));
  }


  useEffect(() => {
      getCampaigns();
    }, []);

  return (
    <>
      <div>
        <HostedCampaignList />
        &nbsp;
        <JoinedCampaignList />
      </div>
    </>
  )
}

export default Campaigns;