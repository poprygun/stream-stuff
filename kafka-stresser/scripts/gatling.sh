#!/usr/bin/env bash

function set_bash_fail_on_error() {
    set -o errexit
    set -o errtrace
    set -o nounset
    set -o pipefail
}

function go_to_kafka_stresser_target_directory() {
    local -r root_directory=$(git rev-parse --show-toplevel)

    cd "$root_directory/kafka-stresser/target/gatling-charts-highcharts-bundle-3.3.0"
}

function run_script_from_bundle() {
    ./bin/gatling.sh
}

function main() {
    set_bash_fail_on_error
    go_to_kafka_stresser_target_directory
    run_script_from_bundle
}

main
