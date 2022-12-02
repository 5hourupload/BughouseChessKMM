//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct RulesView: View {
    @Binding var currentView: String
    
    
    
    var body: some View
    {
        
        VStack( spacing: 16){
            Image(uiImage: UIImage(named: "title")!)
                .resizable()
                .aspectRatio(contentMode: .fit)
            
            Button("Back") {
                
                currentView = "Main Menu View"
            }.foregroundColor(.white)
            
            Spacer()
            Text("Welcome to Bughouse Chess, a popular variation of chess with two chessboards and four players!")
                .foregroundColor(.white)
                .padding(2)
                
            Text("A captured piece on one board is passed onto the teammate. The teammate now has the option to make a regular move or place their new piece wherever on the board. Pawns cannot be place on the 1st or 8th rank. When pawns are promoted and then captured, the piece will revert back to a pawn. The game ends when either board wins. Games can be won either by running out of time, checkmate, or by capturing the king (only possible when playing without checking). Some positions that would be checkmate in traditional chess are not considered checkmate in bughouse, due to the ability to drop pieces anywhere on the board. This does occasionally cause situations where a player is forced to stall. Stalemates are not possible. If a player cannot move a piece already on the board, they must wait for a piece from their teammate. Communication is key between the partners! Have fun!").foregroundColor(.white).padding(12)
            
            
                   
        }

    }
    
}
