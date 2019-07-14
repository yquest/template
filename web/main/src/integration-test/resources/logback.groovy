appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "[%-6relative|%-30thread|%70logger|%6level]  - %msg%n"
  }
}
logger('pt.fabm.template',ALL)
logger('io.vertx.ext.web',TRACE)
root(INFO, ["STDOUT"])
