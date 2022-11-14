package fhu.bughousechess

import fhu.bughousechess.pieces.Piece
import kotlin.jvm.JvmField

class Move {

    @JvmField
    var pieceType = ""
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

    constructor(x: Int, y: Int, x1: Int, y1: Int, type: String) {
        this.x = x
        this.y = y
        this.x1 = x1
        this.y1 = y1
        this.type = type
    }

    constructor(
        pieceType: String,
        x1: Int,
        y1: Int,
        type: String
    ) {

        this.pieceType = pieceType
        this.x1 = x1
        this.y1 = y1
        this.type = type
    }
}