-- noinspection SqlNoDataSourceInspectionForFile

-- Insert users
INSERT INTO USER_ENTITY (profilePicturePath, username, name, surname, password, email, bio, role) VALUES
            ('https://randomuser.me/api/portraits/men/1.jpg', 'john_doe', 'John', 'Doe', '$2a$12$ncJbyg5JgxHi6ShDwgRB2OF8gHJY8vmB1.a1zhx90eSHIt7WIbYGq', 'john.doe@example.com', 'Animal lover and volunteer', 'USER'),
            ('https://randomuser.me/api/portraits/women/2.jpg', 'jane_smith', 'Jane', 'Smith', '$2a$12$ncJbyg5JgxHi6ShDwgRB2OF8gHJY8vmB1.a1zhx90eSHIt7WIbYGq', 'jane.smith@example.com', 'Community activist', 'ADMIN'),
            ('https://randomuser.me/api/portraits/men/3.jpg', 'mike_wilson', 'Mike', 'Wilson', 'pass', 'mike.wilson@example.com', 'Helping people is my passion', 'USER'),
            ('https://randomuser.me/api/portraits/women/4.jpg', 'sarah_jones', 'Sarah', 'Jones', 'pass', 'sarah.jones@example.com', 'Environmental enthusiast', 'USER');

-- PETS category events (3)
INSERT INTO EVENT (category, title, description, details,location, address, startDateTime, upvote, creatorId) VALUES
             ('PETS', 'Pet Shelter Cleanup', 'Help us clean and organize the local animal shelter', 'In facilisis turpis felis, sed ornare elit sollicitudin nec. Quisque efficitur nisi vitae lacinia interdum. Nulla vel bibendum purus. Donec quis rhoncus risus, eu feugiat odio. Vestibulum viverra ornare lobortis. Nunc porttitor quam iaculis ante varius, eu sodales tortor lacinia. Vivamus in ex tristique, volutpat ante quis, aliquam arcu. Nulla ut diam faucibus est imperdiet malesuada. Quisque ac dignissim lectus, id vestibulum diam.','ZAGREB', 'City Animal Shelter', '2023-11-15 09:00:00', 5, 1),
             ('PETS', 'Dog Walking Afternoon', 'Walk dogs from the shelter who need exercise','In facilisis turpis felis, sed ornare elit sollicitudin nec. Quisque efficitur nisi vitae lacinia interdum. Nulla vel bibendum purus. Donec quis rhoncus risus, eu feugiat odio. Vestibulum viverra ornare lobortis. Nunc porttitor quam iaculis ante varius, eu sodales tortor lacinia. Vivamus in ex tristique, volutpat ante quis, aliquam arcu. Nulla ut diam faucibus est imperdiet malesuada. Quisque ac dignissim lectus, id vestibulum diam.', 'SPLIT', 'Paw Park', '2023-11-20 14:00:00', 12, 1),
             ('PETS', 'Pet Food Donation Drive', 'Collecting pet food for strays and shelter animals', 'In facilisis turpis felis, sed ornare elit sollicitudin nec. Quisque efficitur nisi vitae lacinia interdum. Nulla vel bibendum purus. Donec quis rhoncus risus, eu feugiat odio. Vestibulum viverra ornare lobortis. Nunc porttitor quam iaculis ante varius, eu sodales tortor lacinia. Vivamus in ex tristique, volutpat ante quis, aliquam arcu. Nulla ut diam faucibus est imperdiet malesuada. Quisque ac dignissim lectus, id vestibulum diam.','OSIJEK','Town Square', '2025-06-24 14:00:00', 8, 2);

-- PEOPLE category events (3)
INSERT INTO EVENT (category, title, description, details, location, address, startDateTime, upvote, creatorId) VALUES
             ('PEOPLE', 'Homeless Shelter Meal Service', 'Prepare and serve meals at the local homeless shelter', '','SPLIT','Hope Shelter', '2023-11-18 17:00:00', 15, 2),
             ('PEOPLE', 'Senior Citizen Companionship', 'Visit and spend time with elderly residents', '', 'ZAGREB','Sunny Pines Retirement Home', '2023-11-22 13:00:00', 7, 3),
              ('PEOPLE', 'Youth Mentoring Program', 'Mentor underprivileged youth in academics and life skills','','OSIJEK', 'Community Center', '2023-11-26 15:00:00', 10, 3);

-- COMMUNITY category events (3)
INSERT INTO EVENT (category, title, description, details, location, address, startDateTime, upvote, creatorId) VALUES
             ('COMMUNITY', 'Park Cleanup', 'Help clean up trash and debris in the local park','', 'Central Park','SPLIT', '2023-11-19 08:00:00', 20, 4),
             ('COMMUNITY', 'Community Garden Planting', 'Plant vegetables and flowers in the community garden','','OSIJEK', 'Green Acres Garden', '2023-11-23 09:00:00', 14, 4),
             ('COMMUNITY', 'Neighborhood Watch Meeting', 'Organize and plan neighborhood safety initiatives','','ZAGREB', 'Community Hall', '2023-11-27 18:30:00', 6, 1);

-- OTHER category events (3)
INSERT INTO EVENT (category, title, description, details, location, address, startDateTime, upvote, creatorId) VALUES
             ('OTHER', 'Book Drive for Libraries', 'Collect books to donate to schools and public libraries','','ZAGREB', 'Main Library', '2023-11-17 10:00:00', 9, 3),
             ('OTHER', 'Tech Support for Seniors', 'Help senior citizens with technology questions and issues','', 'OSIJEK','Senior Center', '2023-11-21 14:00:00', 11, 2),
             ('OTHER', 'Charity Fun Run', 'Participate in a 5K run to raise money for local charities', '','ZAGREB','Riverfront Park', '2023-11-28 08:00:00', 25, 4);

INSERT INTO "ATTENDING EVENTS" (userEntityId, eventId) VALUES
               (1, 3),
               (2, 3),
               (3, 3),
               (4, 3);

INSERT INTO  EVENT_IMAGE (imagePath, eventId) VALUES
                ('https://picsum.photos/300/300',1),
                ('https://picsum.photos/300/300',1),
                ('https://picsum.photos/300/300',1),
                ('https://picsum.photos/300/300',2),
                ('https://picsum.photos/300/300',2),
                ('https://picsum.photos/300/300',2),
                ('https://picsum.photos/300/300',3),
                ('https://picsum.photos/300/300',3),
                ('https://picsum.photos/300/300',3),
                ('https://picsum.photos/300/300',4),
                ('https://picsum.photos/300/300',4),
                ('https://picsum.photos/300/300',4),
                ('https://picsum.photos/300/300',5),
                ('https://picsum.photos/300/300',5),
                ('https://picsum.photos/300/300',5),
                ('https://picsum.photos/300/300',6),
                ('https://picsum.photos/300/300',6),
                ('https://picsum.photos/300/300',6);