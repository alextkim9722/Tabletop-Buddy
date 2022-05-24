package capstone.service;

import capstone.models.Session;
import capstone.models.SessionUser;

public class SessionService {
    Result<Session> create(Session session) {}
    Result<Session> update(Session session) {}
    boolean delete(int id) {}
    Result<Session> getFromUserId(int id) {}
    Result<Session> getFromCampaignId(int id) {}
    Result<Void> createUser(SessionUser su) {}
    Result<Void> updateUser(SessionUser su) {}
    boolean deleteUser(int id) {}
    Result<Session> validate(Session session) {}
}
