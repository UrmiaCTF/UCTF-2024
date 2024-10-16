cmake_minimum_required(VERSION 3.8)

set(PROJECT_NAME "REVNG")
project(${PROJECT_NAME})


include_directories(
    ./inc
)

set(inc
    ./inc/prng.h
)

set(src
    ./src/prng.cpp
    ./src/main.cpp
)

# add_compile_options(
#     -g
# )


add_executable(${PROJECT_NAME} ${src} ${inc})
set_target_properties(${PROJECT_NAME} PROPERTIES LINK_FLAGS "-s")