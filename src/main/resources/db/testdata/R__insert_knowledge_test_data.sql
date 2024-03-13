DELETE FROM `knowledge_point`;
DELETE FROM `pre_knowledge_relation`;

INSERT INTO `knowledge_point` (id, name, type, outline_id)
VALUES
(uuid(), 'Linear Relationships', 'topic', null),
(uuid(), 'Basic Concepts of Cartesian Coordinate System', 'sub_topic', null),
(uuid(), 'Number Patterns', 'sub_topic', null),
(uuid(), 'Linear Equations', 'sub_topic', null),
(uuid(), 'Linear Relationships and Graphs', 'sub_topic', null),
(uuid(), 'Cartesian plane and ordered pairs', 'content', null),
(uuid(), 'Mid-point formula', 'content', null),
(uuid(), 'Distance formula', 'content', null),
(uuid(), 'Order pairs', 'sub_content', '1'),
(uuid(), 'Cartesian plane', 'sub_content', '2'),
(uuid(), 'Applications of ordered pairs', 'sub_content', '3'),
(uuid(), 'Plotting coordinates on Cartesian planes', 'sub_content', '4'),
(uuid(), 'Mid-point', 'sub_content', '5'),
(uuid(), 'Distance between two points', 'sub_content', '6')
;

SET @topic_id = (select id from knowledge_point where name = 'Linear Relationships');
SET @sub_topic_id = (select id from knowledge_point where name = 'Basic Concepts of Cartesian Coordinate System');

SET @content_id1 = (select id from knowledge_point where name = 'Cartesian plane and ordered pairs');
SET @content_id2 = (select id from knowledge_point where name = 'Mid-point formula');
SET @content_id3 = (select id from knowledge_point where name = 'Distance formula');

SET @sub_content_id1 = (select id from knowledge_point where name = 'Order pairs');
SET @sub_content_id2 = (select id from knowledge_point where name = 'Cartesian plane');
SET @sub_content_id3 = (select id from knowledge_point where name = 'Applications of ordered pairs');
SET @sub_content_id4 = (select id from knowledge_point where name = 'Plotting coordinates on Cartesian planes');
SET @sub_content_id5 = (select id from knowledge_point where name = 'Mid-point');
SET @sub_content_id6 = (select id from knowledge_point where name = 'Distance between two points');

update knowledge_point set parent_id = @topic_id
where name in (
'Basic Concepts of Cartesian Coordinate System',
'Number Patterns',
'Linear Equations',
'Linear Relationships and Graphs'
);

update knowledge_point set parent_id = @sub_topic_id
where name in (
'Cartesian plane and ordered pairs',
'Mid-point formula',
'Distance formula'
);

update knowledge_point set parent_id = @content_id1
where name in (
'Order pairs',
'Cartesian plane',
'Applications of ordered pairs',
'Plotting coordinates on Cartesian planes'
);

update knowledge_point set parent_id = @content_id2
where name in (
'Mid-point'
);

update knowledge_point set parent_id = @content_id3
where name in (
'Distance between two points'
);

INSERT INTO `pre_knowledge_relation`(id, knowledge_point_id, pre_knowledge_point_id, relevance)
VALUES
(uuid(), @content_id2, @content_id1, 0.3),
(uuid(), @content_id3, @content_id1, 0.3),
(uuid(), @content_id3, @content_id1, 0.3),
(uuid(), @sub_content_id3, @sub_content_id1, 0.8),
(uuid(), @sub_content_id4, @sub_content_id1, 0.5),
(uuid(), @sub_content_id4, @sub_content_id2, 0.7);

insert into `student_focus`(id, student_id, knowledge_point_id)
values
(uuid(), '29895382-d9da-11ee-bc41-0242ac190004', @sub_content_id4);