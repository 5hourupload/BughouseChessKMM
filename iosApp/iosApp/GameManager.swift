//
//  GameManager.swift
//  iosApp
//
//  Created by Alan Shen on 10/26/22.
//  Copyright © 2022 orgName. All rights reserved.
//
import Foundation
import SwiftUI
import shared
final class GameManager: ObservableObject {
    
    @Published var counter: Int = 0
    @Published var board = [[[BoardSquare]]](repeating: [[BoardSquare]](), count: 2)
    @Published var roster0W = [RosterSquare]()
    @Published var roster0B = [RosterSquare]()
    @Published var roster1W = [RosterSquare]()
    @Published var roster1B = [RosterSquare]()
    var gms = GameStateManager()
    
    var moves: [Set<Move>] = [Set(), Set()]
    
    @Published var showPawnOptions = [false, false]
    var pawnPromotionX = [-1, -1];
    var pawnPromotionY = [-1, -1];
    var pawnPromotionColor = ["", ""];
    
    @Published var showGameEndScreen = false
    @Published var controlButtonText = "Start"
    var times = [300*1000, 300*1000, 300*1000, 300*1000]


    
    
    init(counter: Int)  {
        self.counter = counter
        initBoard(boardNumber: 0)
        updatePieces()
    }
    func initBoard(boardNumber: Int)
    {
        if (boardNumber == 0)
        {
            for b in 0...1 {
                for _ in 0...7 {
                    board[b].append([BoardSquare(), BoardSquare(), BoardSquare(), BoardSquare(), BoardSquare(), BoardSquare(), BoardSquare(), BoardSquare()])
                }
            }
        }
        for _ in 0...30 {
            roster0W.append(RosterSquare())
            roster0B.append(RosterSquare())
            roster1W.append(RosterSquare())
            roster1B.append(RosterSquare())
        }
    }
    
    func updatePieces()
    {
        for b in 0...1 {
            for x in 0...7 {
                for y in 0...7 {
                    board[b][x][y].piece = (gms.getPositions(boardNumber: Int32(b)).get(index: Int32(x))?.get(index: Int32(y)))!
                }
            }
        }
        
        for i in 0...29 {
            roster0W[i].piece = gms.roster1p.get(index: Int32(i))!
            roster0B[i].piece = gms.roster2p.get(index: Int32(i))!
            roster1W[i].piece = gms.roster4p.get(index: Int32(i))!
            roster1B[i].piece = gms.roster3p.get(index: Int32(i))!
        }
    }
    public func processMove(x:Int, y: Int, boardNumber: Int)
    {
        if (gms.gameState != GameStateManager.GameState.playing) { return }
        
        for m in moves[boardNumber]
        {
            if (m.x1 == x && m.y1 == y)
            {
  
                if (m.type == "roster")
                {
                    gms.performRosterMove(i: m.i, x: m.x1, y: m.y1, boardNumber: Int32(boardNumber));
                    moves[boardNumber] = Set()
                    updatePieces()

                    clean(boardNumber: boardNumber, leaveCheck: false);
                    if (gms.gameOver) {
                        gameEndProcedures(side: gms.gameOverSide, type: gms.gameOverType);
                        return
                    }
                    var color = gms.turn.get(index: Int32(boardNumber)) == 1 ? "white" : "black";
                    if (gms.checking && gms.inCheck(positions: gms.getPositions(boardNumber: Int32(boardNumber)),color: color,boardNumber: Int32(boardNumber)))
                    {
                        setCheckUIConditions(color: color,boardNumber: boardNumber);
                    }
                    return
                }
                else
                {
                    gms.performMove(moveType: m.type, x: m.x, y: m.y, x1: Int32(x), y1: Int32(y), boardNumber: Int32(boardNumber));
                    moves[boardNumber] = Set()
                    updatePieces()
                    pawnCheck(boardNumber: boardNumber);
                    clean(boardNumber: boardNumber, leaveCheck: false)
                    if (gms.gameOver) {
                        gameEndProcedures(side: gms.gameOverSide, type: gms.gameOverType);
                        return;
                    }
                    var color = gms.turn.get(index: Int32(boardNumber)) == 1 ? "white" : "black";
                    if (gms.checking && gms.inCheck(positions: gms.getPositions(boardNumber: Int32(boardNumber)),color: color,boardNumber: Int32(boardNumber)))
                    {
                        setCheckUIConditions(color: color,boardNumber: boardNumber);
                    }
                    return
                }
            }
        }
        
        let piece = gms.getPositions(boardNumber: Int32(boardNumber)).get(index: Int32(x))?.get(index: Int32(y))
        if (piece!.color == "white" && gms.turn.get(index: Int32(boardNumber)) != 1) { return }
        if (piece!.color == "black" && gms.turn.get(index: Int32(boardNumber)) != 2) { return }
        
        clean(boardNumber: boardNumber, leaveCheck: true)
        
        let allMoves = piece!.getMoves(positions: gms.getPositions(boardNumber: Int32(boardNumber)), x: Int32(x), y: Int32(y), boardNumber: Int32(boardNumber))
        moves[boardNumber] = Set()
        for m in allMoves
        {
            if (gms.checkIfMoveResultsInCheck(moveType: m.type,x: m.x,y: m.y,x1: m.x1,y1: m.y1,boardNumber: Int32(boardNumber))) {continue}
            
            moves[boardNumber].insert(m)
            
            board[boardNumber][x][y].cosmetic = "yellow"

            if (m.type == "take" || m.type == "whiteEnP" || m.type == "blackEnP")
            {
                board[boardNumber][Int(m.x1)][Int(m.y1)].cosmetic = "red";
            }
            else
            {
                board[boardNumber][Int(m.x1)][Int(m.y1)].cosmetic = "dot"
            }
        }
        
    }
    
    public func processRosterClick(i: Int, roster: [RosterSquare], boardNumber: Int)
    {
        if (gms.gameState != GameStateManager.GameState.playing) { return }
        
        let piece = gms.getCurrentRosterArray(boardNumber: Int32(boardNumber)).get(index: Int32(i))
        if (roster[i].piece.color == "white" && gms.turn.get(index: Int32(boardNumber)) != 1) { return }
        if (roster[i].piece.color == "black" && gms.turn.get(index: Int32(boardNumber)) != 2) { return }
        
        
        clean(boardNumber: boardNumber, leaveCheck: true)
        
        var allMoves = piece!.getRosterMoves(positions: gms.getPositions(boardNumber: Int32(boardNumber)), rosterp: gms.getCurrentRosterArray(boardNumber: Int32(boardNumber)), i: Int32(i))
        moves[boardNumber] = Set()
        for m in allMoves
        {
            if (!gms.rosterMoveIsLegal(rosterPiece: gms.getCurrentRosterArray(boardNumber: Int32(boardNumber)).get(index: Int32(i))!, x: Int32(m.x1), y: (m.y1), boardNumber: Int32(boardNumber))) {continue}
            moves[boardNumber].insert(m)
            roster[i].cosmetic = "yellow"
                        
            board[boardNumber][Int(m.x1)][Int(m.y1)].cosmetic = "dot"
            
        }
    }
    
    func clean(boardNumber: Int, leaveCheck: Bool)
    {
        for x in 0...7 {
            for y in 0...7 {
                if leaveCheck && board[boardNumber][x][y].cosmetic == "blue"
                {
                    continue
                }
                board[boardNumber][x][y].cosmetic = "none"
            }
        }
        if (boardNumber == 0)
        {
            for i in 0...29 {
                roster0W[i].cosmetic = "none"
                roster0B[i].cosmetic = "none"
            }
        }
        if (boardNumber == 1)
        {
            for i in 0...29 {
                roster1W[i].cosmetic = "none"
                roster1B[i].cosmetic = "none"
            }
        }
        
    }
    
    public func start()
    {
        gms.start()
    }
    
    public func pawnCheck(boardNumber: Int)
    {
        let nextTurn = gms.turn.get(index: Int32(boardNumber));
        let y = nextTurn == 2 ? 7 : 0;
        let color = nextTurn == 2 ? "white" : "black";
        for i in 0...7
        {
            let piece = gms.getPositions(boardNumber: Int32(boardNumber)).get(index: Int32(i))!.get(index: Int32(y))
            if (piece!.color == color && piece!.type == "pawn")
            {
                pawnPromotionX[boardNumber] = i
                pawnPromotionY[boardNumber] = y
                pawnPromotionColor[boardNumber] = color

                gms.pause(boardNumber: Int32(boardNumber));
                showPawnOptions[boardNumber] = true;
            }

        }
    }
    
    public func selectPromotionPiece(piece: String, boardNumber: Int)
    {
        var newPiece: Piece
        if piece == "queen" { newPiece = Queen(color: pawnPromotionColor[boardNumber]) }
        else if piece == "rook" { newPiece = Rook(color: pawnPromotionColor[boardNumber]) }
        else if piece == "bishop" { newPiece = Bishop(color: pawnPromotionColor[boardNumber]) }
        else { newPiece = Knight(color: pawnPromotionColor[boardNumber]) }
        
        gms.promote(x: Int32(pawnPromotionX[boardNumber]), y: Int32(pawnPromotionY[boardNumber]), piece: newPiece, boardNumber: Int32(boardNumber));
        updatePieces()
        showPawnOptions[boardNumber] = false

        if (gms.gameState == GameStateManager.GameState.playing)
        {
            gms.resume(boardNumber: Int32(boardNumber));
        }
    }
    
    public func gameEndProcedures(side: Int32, type: Int32)
    {
        gms.gameEndProcedures(side: side, type: type);
        clean(boardNumber: 0, leaveCheck: true);
        clean(boardNumber: 1, leaveCheck: true);
        showGameEndScreen = true
        controlButtonText = "Start"
        
    }
    
    private func setCheckUIConditions(color: String, boardNumber: Int)
    {
        for i in 0...7
        {
            for j in 0...7
            {
                if gms.getPositions(boardNumber: Int32(boardNumber)).get(index: Int32(i))?.get(index: Int32(j))!.color == color && gms.getPositions(boardNumber: Int32(boardNumber)).get(index: Int32(i))?.get(index: Int32(j))!.type == "king"
                {
                    board[boardNumber][i][j].cosmetic = "blue";
                }
            }
        }
    }
    
    public func newGame()
    {
        gms = GameStateManager()
        updatePieces()
        showPawnOptions[0] = false
        showPawnOptions[1] = false
        times = [300*1000, 300*1000, 300*1000, 300*1000]
    }
    
    public func controlButtonClick()
    {
        if (gms.gameState == GameStateManager.GameState.pregame)
        {
            newGame();
//                        scroll1.fullScroll(View.FOCUS_UP);
//                        scroll3.fullScroll(View.FOCUS_UP);
//                        scroll2.fullScroll(View.FOCUS_DOWN);
//                        scroll4.fullScroll(View.FOCUS_DOWN);
            start()
            controlButtonText = "Pause"
        }
        else if (gms.gameState == GameStateManager.GameState.paused)
        {
            gms.resume();
            controlButtonText = "Pause"
        }
        else if gms.gameState == GameStateManager.GameState.playing
        {
            clean(boardNumber: 0,leaveCheck: true);
            clean(boardNumber: 1, leaveCheck: true);
            gms.pause();
            controlButtonText = "Resume";
        }
    }
}
