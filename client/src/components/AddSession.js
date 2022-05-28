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

function AddSession() {
  const [event, setEvents] = useState([]);
  const [successfulDelete, setSuccessfulDelete] = useState(false);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [startCalendarDate, setStartCalendarDate] = useState(null);
  const [endCalendarDate, setEndCalendarDate] = useState(null);
  const [addingUserSchedule, setAddingUserSchedule] = useState(false);
  const [errors, setErrors] = useState([]);
  const history = useHistory();
  const authManager = useContext(AuthContext);

  let getSessions = () => {
    return fetch(`http://localhost:8080/api/userSchedule/${authManager.userId}`)
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
          title:body[i].sessionId,
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

  useEffect(() => {
    getSessions();
  }, []);

  const handleUserScheduleDelete = (id) => {
    const init = {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
      }
    }

    fetch(`http://localhost:8080/api/userSchedule/${id}`, init)
    .then(response => {
      if (response.status === 204) {
        setSuccessfulDelete(true);
        return;
      }

      return Promise.reject('Something went wrong :)');
    })
    .catch(err => console.error(err));
  }

  const handleUserScheduleAdd = () => {
    const newUserSchedule = {
      userId: authManager.userId,
      sessionId: 0,
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

    fetch('http://localhost:8080/api/userSchedule', init)
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
        history.push('/campaign');
      } else {
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
    let time = prompt('Please enter the time that you want');
    var timeDiff = moment(time, "HH:mm");
    let newDate = moment(clickInfo.date);
    newDate.add(timeDiff.hour(), 'hour');
    newDate.add(timeDiff.minute(), 'minute');
  }

  const handleAddingFlag = () => {
    setAddingUserSchedule(!addingUserSchedule);
  }

  return (
    <div className="Add Session">
      <Errors errors={errors}/>
      <div className="form-group">
        {!addingUserSchedule ?
        <button className="btn btn-primary" type="submit" onClick={handleAddingFlag}>Add</button>
        :
        <button className="btn btn-secondary" type="button" onClick={handleAddingFlag}>Cancel</button>
        }
      </div>
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
  );
}

export default AddSession;