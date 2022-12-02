//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct MainMenuView: View {
    @Binding var currentView: String
    
   
    
    var body: some View
    {
        ZStack {
            Color(hex: "FF1E1E1E").ignoresSafeArea()
            VStack{
                Image(uiImage: UIImage(named: "title")!)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                
                HStack(spacing: 0)
                {
                    Spacer()
                    VStack()
                    {
                        Button("play") {
                            currentView = "Game View"
                        }.foregroundColor(Color.white)
                            .font(Font.custom("Montepetrum", size: 75))
                        Button("options") {
                            currentView = "Options View"
                        }
                        .foregroundColor(Color.white)
                        .font(Font.custom("Montepetrum", size: 75))
                        
                        Button("rules") {
                            currentView = "Rules View"
                        }
                        .foregroundColor(Color.white)
                        .font(Font.custom("Montepetrum", size: 75))

                    }
                    Spacer()
                    Image(uiImage: UIImage(named: "main_menu_side_image")!)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                }
                
            }
        }
        

    }
    
}

