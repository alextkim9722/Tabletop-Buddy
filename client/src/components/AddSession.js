import React from 'react'
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import { useEffect, useState, useContext } from "react";

import AuthContext from "../AuthContext";

function AddSession() {
  const [event, setEvents] = useState([]);
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

  const handleEventClick = (clickInfo) => {
    if (confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
      clickInfo.event.remove()
    }
  }

  return (
    <div className="Add Session">
      {console.log(event)}
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
        weekendsVisible={true}
        events={event}
        eventClick={handleEventClick}
        navLinks={true}
      />
    </div>
  );
}

export default AddSession;