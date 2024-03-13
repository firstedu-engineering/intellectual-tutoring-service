
CREATE TABLE `knowledge_point` (
  `id` char(36) NOT NULL,
  `name` varchar(200) NOT NULL,
  `type` enum('area_of_study','topic','sub_topic','content','sub_content') NOT NULL,
  `parent_id` char(36),
  `outline_id` char(36),
  `created_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
