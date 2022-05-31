import Errors from "./Errors";
import { useState, useEffect } from "react"; 
import { useHistory } from "react-router-dom";

function DeleteUser() {
    const [errors, setErrors] = useState([]);
    const history = useHistory();
    const [username, setUsername] = useState('');
    const [user, setUser] = useState({}); 
    const [deleteNow, setDeleteNow] = useState(false); 

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
      } 
    
    
    useEffect(() => { if (deleteNow) {
        if(user !== {}){
        const init = {
            method: 'DELETE',
            headers: {
                Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
            }
    }
    fetch(`${window.TABLETOPBUDDY_ROOT_URL}/user/${user.userId}`, init)
    .then(response => {
        switch (response.status) {
            case 204: 
            history.push('/');
            return;
            case 403:
            history.push('/'); 
            break
            default:
            return Promise.reject('Something went wrong :)');
        }
    })
        .then(body => {
            if (!body) {
              history.push('/');
              return;
            }
            setErrors(body);
          })
          .catch(err => console.error(err));
    }
        
    }
        
        }, [deleteNow])


    const handleSubmit = (event) => {
        event.preventDefault();
        const init = {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
            }
        };

        return fetch(`${window.TABLETOPBUDDY_ROOT_URL}/user/${username}`, init)
        .then(response => {
            switch (response.status) {
                case 200:
                    return response.json(); 
                case 404: 
                    history.replace('/not-found');
                    break
                case 403: 
                    history.push('/');
                    break
                default:
                    return Promise.reject('Something went wrong on the server :)');
            }
            
    })
    .then(json => {
        setUser(json);
        setDeleteNow(true); 
      })
      .catch(err => console.error(err));
}


return (
<>
<form>
<div className="form-group"></div>
<label htmlFor="name">Enter a Username:</label>
    <input className="form-control" type="text" id="username" name="username" value={username} onChange={handleUsernameChange} ></input>
    &nbsp; 
    <div className="form-group">
              <button className="btn btn-primary" type="submit" onClick={handleSubmit}>Delete</button>
              &nbsp;
              <button className="btn btn-secondary" type="button" onClick={() => history.push('/')}>Cancel</button>
    </div>
</form>
<Errors errors={errors}/>
</>
); 
}    

export default DeleteUser; 