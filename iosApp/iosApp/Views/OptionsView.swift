//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct OptionsView: View {
    var gm: GameManager
    @Binding var currentView: String
    
    @AppStorage("minutes") var minutes = 5
    @AppStorage("seconds") var seconds = 0

    @AppStorage("checking") var checkingToggle = true
    @AppStorage("placing") var placingToggle = true
    @AppStorage("reverting") var revertingToggle = true
    @AppStorage("firstrank") var firstrankToggle = false

    
    var body: some View
    {
        
        VStack{
            Button("Back") {
                gm.getUserDefaults()
                
                currentView = "Game View"
            }
            
            Form {
                Button("New Game"){
                    gm.newGame()
                    currentView = "Game View"

                }
                Section(header: Text("Time"), footer: Text("Time changes apply on new game."))
                    {

                            Picker("Minutes", selection: $minutes)
                            {
                                ForEach(0...20, id: \.self)
                                {
                                    Text("\($0)")
                                }
                            }
                        Picker("Seconds", selection: $seconds)
                            {
                                ForEach(0...59, id: \.self)
                                {
                                    Text("\($0)")
                                }
                            }
                        
                        
                        
                    }
                    
                
                
                Section(header: Text("Rules"), footer: Text("If checking is off, the king maybe be captured to win the game. Checkmate still applies."))
                {
                    Toggle(isOn: $checkingToggle,
                        label: {Text("Checking")})
                }
                Section(footer: Text("When turned off, pieces cannot be placed in checking positions."))
                {
                    Toggle(isOn: $placingToggle,
                        label: {Text("Placing In Check")})
                    
                }
                Section(footer: Text("Revert captured promoted pieces to pawns."))
                {
                    Toggle(isOn: $revertingToggle,
                        label: {Text("Pawn Reversion")})
                    
                }
                Section(footer: Text("Allows placing of pawns on the first rank."))
                {
                    Toggle(isOn: $firstrankToggle,
                        label: {Text("Placing Pawns on 1st Rank")})
                    
                }
            }
        }

    }
    
}
