class UpdateOnly {
  def update(x: Int, y: Int) {
    
  }
}

val x = new UpdateOnly
x(2<caret>) = 3
//TEXT: x: Int, STRIKEOUT: false