import React from 'react'
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import { useEffect, useState, useContext } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";
import Errors from "./Errors";
import moment from 'moment';

function UserScheduleList() {
  const [event, setEvents] = useState([]);
  const [successfulDelete, setSuccessfulDelete] = useState(false);
  const [sessionId, setSessionId] = useState(0);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [adding, setAdding] = useState(false);
  const [errors, setErrors] = useState([]);
  const history = useHistory();
  const authManager = useContext(AuthContext);

  let getUserSchedule = () => {
    return fetch(`${window.TABLETOPBUDDY_ROOT_URL}/userSchedule/${authManager.userId}`)
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
          title:body[i].userScheduleId,
          start:body[i].startDate,
          end:body[i].endDate,
          id:body[i].userScheduleId
        }
        eventList.push(newEvent);
      }

      setEvents(eventList);
    })
    .catch(err => console.error(err));
  }

  useEffect(() => {
    if(endDate !== ''){
      handleUserScheduleAdd();
    }
  }, [endDate])

  useEffect(() => {
    getUserSchedule();
  }, []);

  const handleUserScheduleDelete = (id) => {
    const init = {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
      }
    }

    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/userSchedule/${id}`, init)
    .then(response => {
      if (response.status === 204) {
        setSuccessfulDelete(true);
        getUserSchedule();
        return;
      }
      return Promise.reject('Something went wrong :)');
    })
    .catch(err => console.error(err));
  }

  const handleUserScheduleAdd = () => {
    const newUserSchedule = {
      userId: authManager.userId,
      sessionId,
      startDate,
      endDate
    };

    const init = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
      },
      body: JSON.stringify(newUserSchedule)
    };

    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/userSchedule`, init)
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
      if (json.userScheduleId) {
        getUserSchedule();
      }else{
        setErrors(json);
      }
    })
    .catch(err => console.error(err));
  }

  const handleEventClick = (clickInfo) => {
    if (window.confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
      handleUserScheduleDelete(clickInfo.event.id);
      if(successfulDelete){
        clickInfo.event.remove();
        setSuccessfulDelete(false);
      }
    }
  }

  const handleDateClick = (clickInfo) => {
    let time = !adding ? prompt('Please enter the start time that you want') : prompt('Please enter the end time that you want');
    var timeDiff = moment(time, "HH:mm");
    let newDate = moment(clickInfo.date);
    newDate.add(timeDiff.hour(), 'hour');
    newDate.add(timeDiff.minute(), 'minute');

    if(!adding && timeDiff.isValid()) {
      setStartDate(newDate.toDate());
      setAdding(true);
    }else if(adding && timeDiff.isValid()) {
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
          events={event}
          eventClick={handleEventClick}
          dateClick={handleDateClick}
          navLinks={true}
        />
      </div>
    </>
  );
}

export default UserScheduleList;