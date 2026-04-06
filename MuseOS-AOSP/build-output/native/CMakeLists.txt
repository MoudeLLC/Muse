cmake_minimum_required(VERSION 3.18)
project(MuseOS-Native)

set(CMAKE_CXX_STANDARD 17)

# MuseOS Core
add_library(museos_core SHARED
    museos-core/museos_core.cpp
)
target_link_libraries(museos_core log android)

# Glass Compositor
add_library(museos_glass SHARED
    museos-glass/glass_compositor.cpp
)
target_link_libraries(museos_glass log android GLESv3 EGL)

# AI Engine
add_library(museos_ai SHARED
    museos-ai/ai_engine.cpp
)
target_link_libraries(museos_ai log android)
