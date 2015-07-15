ALTER TABLE lesson_item DROP FOREIGN KEY FK_lesson_item_lesson_id
ALTER TABLE lesson DROP FOREIGN KEY FK_lesson_client_id
ALTER TABLE lesson DROP FOREIGN KEY FK_lesson_tutor_id
ALTER TABLE lesson DROP FOREIGN KEY FK_lesson_calendar
ALTER TABLE calendar_snapshot DROP FOREIGN KEY FK_calendar_snapshot_client_id
ALTER TABLE calendar_snapshot DROP FOREIGN KEY FK_calendar_snapshot_tutor_id
DROP TABLE lesson_item
DROP TABLE calendar_entry
DROP TABLE tutor
DROP TABLE lesson
DROP TABLE calendar_snapshot
DROP TABLE client
