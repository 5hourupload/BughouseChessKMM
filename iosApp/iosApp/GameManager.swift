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
    var minutes: Int = 0
    var seconds: Int = 0
    @Published var times = [300*1000, 300*1000, 300*1000, 300*1000]
    
    init()  {
        initBoard(boardNumber: 0)
        updatePieces()
        getUserDefaults()
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
        roster0W.append(RosterSquare(pieceType: "pawn", color: "white"))
        roster0W.append(RosterSquare(pieceType: "knight", color: "white"))
        roster0W.append(RosterSquare(pieceType: "bishop", color: "white"))
        roster0W.append(RosterSquare(pieceType: "rook", color: "white"))
        roster0W.append(RosterSquare(pieceType: "queen", color: "white"))
        
        roster0B.append(RosterSquare(pieceType: "pawn", color: "black"))
        roster0B.append(RosterSquare(pieceType: "knight", color: "black"))
        roster0B.append(RosterSquare(pieceType: "bishop", color: "black"))
        roster0B.append(RosterSquare(pieceType: "rook", color: "black"))
        roster0B.append(RosterSquare(pieceType: "queen", color: "black"))
        
        roster1W.append(RosterSquare(pieceType: "pawn", color: "white"))
        roster1W.append(RosterSquare(pieceType: "knight", color: "white"))
        roster1W.append(RosterSquare(pieceType: "bishop", color: "white"))
        roster1W.append(RosterSquare(pieceType: "rook", color: "white"))
        roster1W.append(RosterSquare(pieceType: "queen", color: "white"))
        
        roster1B.append(RosterSquare(pieceType: "pawn", color: "black"))
        roster1B.append(RosterSquare(pieceType: "knight", color: "black"))
        roster1B.append(RosterSquare(pieceType: "bishop", color: "black"))
        roster1B.append(RosterSquare(pieceType: "rook", color: "black"))
        roster1B.append(RosterSquare(pieceType: "queen", color: "black"))
        
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
        
        roster0W[0].quantity = gms.captured0W["pawn"] as! Int
        roster0W[1].quantity = gms.captured0W["knight"] as! Int
        roster0W[2].quantity = gms.captured0W["bishop"] as! Int
        roster0W[3].quantity = gms.captured0W["rook"] as! Int
        roster0W[4].quantity = gms.captured0W["queen"] as! Int
        
        roster0B[0].quantity = gms.captured0B["pawn"] as! Int
        roster0B[1].quantity = gms.captured0B["knight"] as! Int
        roster0B[2].quantity = gms.captured0B["bishop"] as! Int
        roster0B[3].quantity = gms.captured0B["rook"] as! Int
        roster0B[4].quantity = gms.captured0B["queen"] as! Int
        
        roster1W[0].quantity = gms.captured1W["pawn"] as! Int
        roster1W[1].quantity = gms.captured1W["knight"] as! Int
        roster1W[2].quantity = gms.captured1W["bishop"] as! Int
        roster1W[3].quantity = gms.captured1W["rook"] as! Int
        roster1W[4].quantity = gms.captured1W["queen"] as! Int
        
        roster1B[0].quantity = gms.captured1B["pawn"] as! Int
        roster1B[1].quantity = gms.captured1B["knight"] as! Int
        roster1B[2].quantity = gms.captured1B["bishop"] as! Int
        roster1B[3].quantity = gms.captured1B["rook"] as! Int
        roster1B[4].quantity = gms.captured1B["queen"] as! Int
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
                    gms.performRosterMove(pieceType: m.pieceType, x: m.x1, y: m.y1, boardNumber: Int32(boardNumber));
                    moves[boardNumber] = Set()
                    updatePieces()

                    clean(boardNumber: boardNumber, leaveCheck: false);
                    if (gms.gameOver) {
                        gameEndProcedures(side: gms.gameOverSide, type: gms.gameOverType);
                        return
                    }
                    let color = gms.turn.get(index: Int32(boardNumber)) == 1 ? "white" : "black";
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
                    let color = gms.turn.get(index: Int32(boardNumber)) == 1 ? "white" : "black";
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
            if (gms.checkIfMoveResultsInCheck(moveType: m.type,x: m.x,y: m.y,x1: m.x1,y1: m.y1, color: piece!.color!, boardNumber: Int32(boardNumber))) {continue}
            
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
    
    public func processRosterClick(square: RosterSquare, boardNumber: Int)
    {
        if (gms.gameState != GameStateManager.GameState.playing) { return }
        
        let pieceType = square.pieceType
        let color = square.color
        
        if (color == "white" && gms.turn.get(index: Int32(boardNumber)) != 1) { return }
        if (color == "black" && gms.turn.get(index: Int32(boardNumber)) != 2) { return }
        
        
        clean(boardNumber: boardNumber, leaveCheck: true)
        
        let allMoves = gms.getRosterMoves(positions: gms.getPositions(boardNumber: Int32(boardNumber)), pieceType: pieceType, color: color)
        moves[boardNumber] = Set()
        for m in allMoves
        {
            if (!gms.rosterMoveIsLegal(pieceType: pieceType, color: color, x: Int32(m.x1), y: (m.y1), boardNumber: Int32(boardNumber))) {continue}
            moves[boardNumber].insert(m)
            square.cosmetic = "yellow"
                        
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
            for i in 0...4 {
                roster0W[i].cosmetic = "none"
                roster0B[i].cosmetic = "none"
            }
        }
        if (boardNumber == 1)
        {
            for i in 0...4 {
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
        gms.promote(x: Int32(pawnPromotionX[boardNumber]), y: Int32(pawnPromotionY[boardNumber]), piece: piece, color: pawnPromotionColor[boardNumber], boardNumber: Int32(boardNumber));
        updatePieces()
        showPawnOptions[boardNumber] = false

        let color = gms.turn.get(index: Int32(boardNumber)) == 1 ? "white" : "black";
        if (gms.checking && gms.inCheck(positions: gms.getPositions(boardNumber: Int32(boardNumber)),color: color,boardNumber: Int32(boardNumber)))
        {
            setCheckUIConditions(color: color, boardNumber: boardNumber);
        }
        if (gms.gameOver) {
            gameEndProcedures(side: gms.gameOverSide, type: gms.gameOverType);
            return;
        }
        
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
        getUserDefaults()
        clean(boardNumber: 0, leaveCheck: false)
        clean(boardNumber: 1, leaveCheck: false)
    }
    
    public func controlButtonClick()
    {
        if (gms.gameState == GameStateManager.GameState.pregame)
        {
            newGame();
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
    
    public func getUserDefaults()
    {
        let firstOpen = UserDefaults.standard.bool(forKey: "opened")
        if !firstOpen {
            UserDefaults.standard.set(true, forKey: "opened")
            UserDefaults.standard.set(5, forKey: "minutes")
            UserDefaults.standard.set(0, forKey: "seconds")
            UserDefaults.standard.set(true, forKey: "checking")
            UserDefaults.standard.set(true, forKey: "placing")
            UserDefaults.standard.set(true, forKey: "reverting")
            UserDefaults.standard.set(false, forKey: "firstrank")
        }
        minutes = UserDefaults.standard.integer(forKey: "minutes")
        seconds = UserDefaults.standard.integer(forKey: "seconds")
        
        gms.checking = UserDefaults.standard.bool(forKey: "checking")
        gms.placing = UserDefaults.standard.bool(forKey: "placing")
        gms.reverting = UserDefaults.standard.bool(forKey: "reverting")
        gms.firstrank = UserDefaults.standard.bool(forKey: "firstrank")
        
        let time = 1000 * (minutes * 60 + seconds)
        times = [time, time, time, time]
    }
}
