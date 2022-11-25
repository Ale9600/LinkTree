ALTER TABLE md_page DROP COLUMN  `value`;
ALTER TABLE md_page DROP COLUMN `key`;
ALTER TABLE md_page DROP COLUMN prova;

drop table if exists md_menu;

drop table if exists i18n_ingredient;
drop table if exists i18n_course;
drop table if exists i18n_intolerance;
drop table if exists md_menu_detail;
drop table if exists md_order_detail;
drop table if exists md_order;
drop table if exists md_ingredient_intolerance_link;
drop table if exists md_course_intolerance_link;
drop table if exists md_intolerance;
drop table if exists md_ingredient;
drop table if exists md_course;