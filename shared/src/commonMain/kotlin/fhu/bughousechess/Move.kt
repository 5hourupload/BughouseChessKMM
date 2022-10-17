package fhu.bughousechess

import fhu.bughousechess.pieces.Piece

class Move {
    var positions: Array<Array<Piece>>
    var rosterp: Array<Piece>
    @JvmField
    var i = 0
    @JvmField
    var x = 0
    @JvmField
    var y = 0
    @JvmField
    var x1: Int
    @JvmField
    var y1: Int
    @JvmField
    var type: String

    constructor(positions: Array<Array<Piece>>, x: Int, y: Int, x1: Int, y1: Int, type: String) {
        this.positions = positions
        this.x = x
        this.y = y
        this.x1 = x1
        this.y1 = y1
        this.type = type
        rosterp = arrayOf<Piece>()
    }

    constructor(
        positions: Array<Array<Piece>>,
        rosterp: Array<Piece>,
        i: Int,
        x1: Int,
        y1: Int,
        type: String
    ) {
        this.positions = positions
        this.rosterp = rosterp
        this.i = i
        this.x1 = x1
        this.y1 = y1
        this.type = type
    }
}