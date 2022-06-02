import React from 'react'
import FullCalendar, { createEventInstance } from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import { useEffect, useState, useContext } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";
import Errors from "./Errors";
import moment from 'moment';

function SessionList(props) {
  const [event, setEvents] = useState([]);
  const [gmEvents, setGmEvents] = useState([]);
  const [userEvent, setUserEvents] = useState([]);
  const [successfulDelete, setSuccessfulDelete] = useState(false);
  const [startDate, setStartDate] = useState('');
  const [CampaignId, setCampaignId] = useState(props.campaign.campaignId);
  const [userList, setUserList] = useState(props.campaign.userList);
  const [currentId, setCurrentId] = useState(``);
  const [endDate, setEndDate] = useState('');
  const [adding, setAdding] = useState(false);
  const [addUsers, setAddingUsers] = useState(false);
  const [errors, setErrors] = useState([]);
  const history = useHistory();
  const authManager = useContext(AuthContext);

  const updateSessionCount = () => {
    const updatedCampaign = {
      campaignId:CampaignId,
      name: props.campaign.name,
      userId: authManager.userId,
      description: props.campaign.description,
      type: props.campaign.type,
      city: props.campaign.city,
      state: props.campaign.state,
      maxPlayers: props.campaign.maxPlayers
    }

    const init = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
        },
        body: JSON.stringify(updatedCampaign)
    };
      
    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign/${CampaignId}`, init)
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

  const getUserSession = () => {
    const tempUserList = props.campaign.userList;

    console.log(tempUserList);

    for(let i = 0;i < tempUserList.length;i++) {
      fetch(`${window.TABLETOPBUDDY_ROOT_URL}/userSchedule/${tempUserList[i].user.userId}`)
      .then(response => {
          if (response.status ===200) {
              return response.json()
          }
          return Promise.reject('Something went wrong on the server :)');
      })
      .then(body => {
        const tempList = body.filter( (s) => (s.session.campaignId !== props.campaign.campaignId) );
        const eventList = [];

        for(let j = 0;j < tempList.length;j++) {
          const newEvent = {
            title:tempUserList[i].user.username,
            start:tempList[j].startDate,
            end:tempList[j].endDate,
            id:tempList[j].userScheduleId
          }
          eventList.push(newEvent);
        }

        setUserEvents(eventList);
      })
      .catch(err => console.error(err));
    }

    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/userSchedule/${authManager.userId}`)
      .then(response => {
          if (response.status ===200) {
              return response.json()
          }
          return Promise.reject('Something went wrong on the server :)');
      })
      .then(body => {
        const tempList = body.filter( (s) => (s.session.campaignId !== props.campaign.campaignId) );
        const eventList = [];

        for(let j = 0;j < tempList.length;j++) {
          const newEvent = {
            title:authManager.user,
            start:tempList[j].startDate,
            end:tempList[j].endDate,
            id:tempList[j].userScheduleId
          }
          eventList.push(newEvent);
        }

        setGmEvents(eventList);
      })
      .catch(err => console.error(err));
  }

  const getSession = () => {
    const id = props.campaign.campaignId;

    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/session/camp/${id}`)
    .then(response => {
        if (response.status ===200) {
            return response.json()
        }
        return Promise.reject('Something went wrong on the server :)');
    })
    .then(body => {
      const eventList = [];

      for(let i = 0;i < body.length;i++) {
        const newEvent = {
          title:props.campaign.name,
          start:body[i].startDate,
          end:body[i].endDate,
          id:body[i].sessionId
        }
        eventList.push(newEvent);
      }

      setEvents(eventList);
    })
    .catch(err => console.error(err));
  }

  const handleUserAdds = (id, sessionId) => {
    const newUserSchedule = {
      sessionId: sessionId,
      userId:id,
      startDate,
      endDate
    };

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

  useEffect(() => {
    if(endDate !== ''){
      handleSessionAdd();
      updateSessionCount();
      let addUsers = true;
      setAddingUsers(addUsers);
    }
  }, [endDate]);

  /*
  useEffect(() => {
    console.log(currentId);
    if(endDate !== '' && addUsers && event.length > 0) {
      for(let i = 0;i < userList.length;i++) {
        handleUserAdds(userList[i].user.userId);
      }

      setAddingUsers(false);
    }
  }, [currentId])
  */

  useEffect(() => {
    setCampaignId(props.campaign.campaignId);
    setUserList(props.campaign.userList);
    if(props.campaign.campaignId) {
      getSession();
      getUserSession();
    }
  }, [props.campaign.campaignId]);

  const handleSessionDelete = (id) => {
    if (authManager.userId == props.campaign.userId){
      const init = {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
        }
      }

      fetch(`${window.TABLETOPBUDDY_ROOT_URL}/session/${id}`, init)
      .then(response => {
        if (response.status === 204) {
          setSuccessfulDelete(true);
          getSession();
          return;
        }
        return Promise.reject('Something went wrong :)');
      })
      .catch(err => console.error(err));
    }
  }

  const handleSessionAdd = () => {
    const newSession = {
      CampaignId,
      startDate,
      endDate
    };

    const init = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
      },
      body: JSON.stringify(newSession)
    };

    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/session`, init)
    .then(response => {
      switch (response.status) {
        case 201:
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
    .then(json => {
      if (json.sessionId) {
        getSession();
        const id = json.sessionId;

        for(let i = 0;i < userList.length;i++) {
          handleUserAdds(userList[i].user.userId, id);
        }

        handleUserAdds(authManager.userId, id);

        setCurrentId(id);
      }else{
        setErrors(json);
      }
    })
    .catch(err => console.error(err));
  }

  const handleEventClick = (clickInfo) => {
    if (window.confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
      handleSessionDelete(clickInfo.event.id);
      if(successfulDelete){
        clickInfo.event.remove();
        setSuccessfulDelete(false);
        updateSessionCount();
      }
    }
  }

  const handleDateClick = (clickInfo) => {
    let time = !adding ? prompt('Please enter the start time that you want') : prompt('Please enter the end time that you want');
    var timeDiff = moment(time, "HH:mm");
    let newDate = moment(clickInfo.date);
    newDate.add(timeDiff.hour(), 'hour');
    newDate.add(timeDiff.minute(), 'minute');

    if(!adding && timeDiff.isValid() && authManager.userId == props.campaign.userId) {
      setStartDate(newDate.toDate());
      setAdding(true);
    }else if(adding && timeDiff.isValid() && authManager.userId == props.campaign.userId) {
      setEndDate(newDate.toDate());
      setAdding(false);
    }
  }

  return (
    <>
      <div className="form-group">
        <Errors errors={errors}/>
      </div>
      <div className="Add Session">
        <FullCalendar
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          headerToolbar={{
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
          }}
          initialView='dayGridMonth'
          editable={true}
          selectable={true}
          selectMirror={true}
          dayMaxEvents={true}
          events={[...event,...userEvent,...gmEvents]}
          eventClick={handleEventClick}
          dateClick={handleDateClick}
          navLinks={true}
        />
      </div>
    </>
  );
}

export default SessionList;