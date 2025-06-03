schema "main" {
  comment = "Main schema for Connect Four game (SQLite)."
}

table "users" {
  schema = schema.main
  column "id" {
    type = integer
    auto_increment = true
    null = false
  }
  column "username" {
    type = text
    null = false
  }
  column "password_hash" {
    type = text
    null = false
  }
  column "created_at" {
    type = datetime
    null = false
    default = sql("CURRENT_TIMESTAMP")
  }
  primary_key {
    columns = [column.id]
  }
  index "username_unique" {
    unique = true
    columns = [column.username]
  }
}
