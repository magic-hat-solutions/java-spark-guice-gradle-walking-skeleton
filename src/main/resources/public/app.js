var ResultsGrid = React.createClass({
    render: function() {

        var that = this;
        return !this.props.data.length ? null : (
            React.DOM.table({ cellSpacing: 0 },
                React.DOM.thead(null,
                    React.DOM.tr(null,
                        _.pairs(this.props.headers).map(function(kvp) {
                            return React.DOM.th({ key: kvp[0] }, kvp[1]);
                        })
                    )
                ),
                React.DOM.tbody(null,
                    this.props.data.map(function(row, index) {
                        var rows = [
                            React.DOM.tr({ key: index },
                                _.keys(that.props.headers).map(function(column) {
                                    return React.DOM.td({ key: column }, row[column]);
                                })
                            )
                        ];

                        _.each(row["addresses"], function(address, addrIdx) {
                            rows.push(
                                React.DOM.tr({ key: index + '.' + addrIdx },
                                    React.DOM.td({ key: "address", colSpan: _.keys(that.props.headers).length }, address)
                                )
                            );
                        });

                        return rows;
                    })
                )
            )
        );
    }
});

var OffenderSearch = React.createClass({

    getInitialState: function() {
        return {
            name: "",
            data: []    // [{"dateOfBirth":"12/04/1994","trust":"NPS London","officerSurname":"Staff","surname":"JoeOneGG","offenderDisplayName":"JoeOneGG, JimMehWW ZZ ZZ","thirdName":"ZZ","officerForename":"Unallocated","officerDisplayName":"Unallocated Staff","offenderId":"2500035708","secondName":"ZZ","current":"Yes","crn":"X027571","gender":"Female","addresses":[],"firstName":"JimMehWW"},{"dateOfBirth":"23/06/1969","trust":"CPA Northumbria","officerSurname":"Anyld","surname":"JimMehSS","offenderDisplayName":"JimMehSS, JoeMehOO Simon ZZ","thirdName":"ZZ","officerForename":"Annette","officerDisplayName":"Annette ZZ Anyld","offenderId":"2500026274","secondName":"Simon","current":"Yes","crn":"X019537","gender":"Male","addresses":["199, Arlington Rd, Camden, London, NW1 7HA, 26/02/2016"],"firstName":"JoeMehOO"}]
        };
    },

    performSearch: _.debounce(function() {

        var that = this;
        var names = this.state.name.split(' ', 2);
        var forename =  names[0];
        var surname = names.length > 0 ? names[1] : '';

        if (forename && surname) {
          $.getJSON('/people/' + surname + '/' + forename, function(data) {
              that.setState({
                  data: data.results
              });
          });
        } else {
          this.setState({
              data: []
          });
        }
    }, 500),

    render: function() {

        var that = this;
        var searchChange = function(ev) {

            that.setState({
                name: ev.target.value
            });

            that.performSearch();
        };

        return React.DOM.div(null,
            React.DOM.input({
                value: this.state.name,
                onChange: searchChange,
                placeholder: 'Enter name here'
            }),
            React.createElement(ResultsGrid, {
                headers: {
                    crn: 'CRN',
                    offenderDisplayName: 'Offender Name',
                    dateOfBirth: 'Date of Birth',
                    gender: 'Gender',
                    officerDisplayName: 'Officer Name'
                },
                data: this.state.data
            })
        );
    }
});

ReactDOM.render(
    React.createElement(OffenderSearch),
    document.getElementById("offenderSearch")
);
