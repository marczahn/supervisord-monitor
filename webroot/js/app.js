function getColorClassesForStatus(status) {
    if (status == 'STOPPED') {
        return 'yellow darken-3';
    }
    if (status == 'FATAL') {
        return 'red darken-1';
    }
    if (status == 'RUNNING') {
        return 'green darken-1';
    }

    return '';
}

var NodeList = React.createClass({
    getInitialState: function() {
        return {nodes: [], loaded: false};
    },
    render: function() {
        if (!this.state.loaded) {
            return (
                <div className="progress">
                    <div className="indeterminate"></div>
                </div>
            )
        }

        return (
            <div className="row">
                {this.state.nodes.map(function(node, i) {
                    return <NodeItem node={node}/>
                })}
            </div>
        );

        return (
            <div className="node-list">
                {this.state.nodes.map(function(node, i) {
                    return <NodeItem node={node}/>
                })}
            </div>
        );
    },
    componentDidMount: function() {
        var self = this;
        var loadNodes = function() {
            fetch('/nodes', {
                method: 'get',
                cache: false
            }).then(function(response) {
                return response.json();
            }).then(function(nodes) {
                self.setState({
                    nodes: nodes,
                    loaded: true
                });
            });
        };

        window.setInterval(loadNodes, 3000);
    }
});

var NodeItem = React.createClass({
    render: function() {
        var node = this.props.node;
        var stateClass = 'new badge ' + (node.state.name == 'RUNNING' ? 'green' : 'red');
        var classRunning = "collection-item " + getColorClassesForStatus('RUNNING');
        var classStopped= "collection-item " + getColorClassesForStatus('STOPPED');
        var classExited = "collection-item " + getColorClassesForStatus('FATAL');

        var countRunning = node.processes.reduce(function(accumulator, process) {
            if (process.state.name == 'RUNNING') {
                accumulator++;
            }

            return accumulator;
        }, 0);

        var countStopped = node.processes.reduce(function(accumulator, process) {
            if (process.state.name == 'STOPPED') {
                accumulator++;
            }

            return accumulator;
        }, 0);

        var countExited = node.processes.reduce(function(accumulator, process) {
            if (process.state.name == 'FATAL') {
                accumulator++;
            }

            return accumulator;
        }, 0);

        return (
            <div className="col s12 m3">
                <div className="card">
                    <div className="card-content">
                        <span className="card-title">{node.name}<span className={stateClass} data-badge-caption={node.state.name || "UNREACHABLE"}/></span>
                        <ul className="collection">
                            <li className={classRunning}>RUNNING: {countRunning}</li>
                            <li className={classStopped}>STOPPED: {countStopped}</li>
                            <li className={classExited}>EXITED {countExited}</li>
                        </ul>
                    </div>
                </div>
            </div>
        )

        return (
            <section className="node">
                <h4 className="header">{node.name}<span className={stateClass} data-badge-caption={node.state.name}/></h4>
                <table className="processes responsive-table bordered">
                    <thead>
                        <tr>
                            <th>Process</th>
                            <th>PID</th>
                            <th>Up since</th>
                            <th>Down since</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {node.processes.map(function(process, i) {
                            var stateClass = getColorClassesForStatus(process.state.name);

                            var started = (new Date(process.start * 1000)).toUTCString();
                            var stopped = '-';
                            if (process.stop != 0) {
                                started = '-';
                                stopped = (new Date(process.stop * 1000)).toUTCString();
                            }

                            return (
                                <tr className={stateClass}>
                                    <td>{process.name}</td>
                                    <td>{process.pid || "-"}</td>
                                    <td>{started}</td>
                                    <td>{stopped}</td>
                                    <td>{process.state.name}</td>
                                </tr>
                             )
                         })}
                    </tbody>
                </table>
            </section>
        );
    }
});

ReactDOM.render(
    <NodeList />,
    document.getElementById('content')
);