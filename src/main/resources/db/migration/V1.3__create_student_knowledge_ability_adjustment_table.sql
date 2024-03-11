
CREATE TABLE `student_knowledge_ability_adjustment` (
  `id` char(36) NOT NULL,
  `student_id` char(36) NOT NULL,
  `practice_id` char(36) NOT NULL,
  `adjusted` TINYINT(1) NOT NULL,
  `created_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
