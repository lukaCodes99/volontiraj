INSERT into USER_ENTITY(username, password)
values ('korisnik', 'password');


INSERT into EVENT(category, title, description, location, startDateTime, upvote, creatorId)
values
    ('PETS', 'title', 'hello', 'Zagreb', '2025-06-02T00:00:00', 0, 1),
    ('PEOPLE', 'title', 'hello', 'Osijek', '2025-06-01T00:00:00', 2, 1);