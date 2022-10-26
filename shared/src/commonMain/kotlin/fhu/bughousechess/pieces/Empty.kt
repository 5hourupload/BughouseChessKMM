package fhu.bughousechess.pieces

import fhu.bughousechess.Move

class Empty : Piece() {
    init {
        color = ""
        type = ""
        wasPawn = false
        empty = true
    }

    override fun getMoves(
        positions: Array<Array<Piece>>,
        x: Int,
        y: Int,
        boardNumber: Int
    ): Set<Move> {
        return HashSet()
    }
}