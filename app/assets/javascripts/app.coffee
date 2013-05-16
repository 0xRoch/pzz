require [
  "lib/jquery/jquery",
  "lib/angular/angular",
], ->
  console.log "core loaded..."
  require [
    "lib/angular-resource/angular-resource"
    "lib/angular-ui/build/angular-ui",
    "lib/angular-ui/build/angular-ui-ieshiv",
    "lib/angular-bootstrap/ui-bootstrap"
  ], ->
    console.log "angular extensions loaded..."
    require ["zeezoo"], ->
      console.log "app.coffee complete loading"